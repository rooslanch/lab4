package com.example.lab4.simulation.view.excel;

import com.example.lab4.simulation.controller.events.SimulationEvent;
import com.example.lab4.simulation.controller.events.SimulationResetEvent;
import com.example.lab4.simulation.controller.events.StateUpdateEvent;
import com.example.lab4.simulation.controller.observers.SimulationObserver;
import com.example.lab4.simulation.model.dto.SnapshotDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

public class ExcelExporterObserver implements SimulationObserver {

    private final LinkedBlockingQueue<SnapshotDTO> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    private final File outputDir;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private Thread exportThread;
    private long startTimestamp;  // Переменная для хранения времени начала записи

    public ExcelExporterObserver(File outputDir) {
        this.outputDir = outputDir;
        startExportThread();
    }

    private void startExportThread() {
        exportThread = new Thread(() -> {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100); // буфер на 100 строк
            Sheet sheet = workbook.createSheet("Simulation");
            int rowNum = 0;

            // Заголовки
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("X");
            header.createCell(1).setCellValue("V");
            header.createCell(2).setCellValue("A");
            header.createCell(3).setCellValue("Slope");
            header.createCell(4).setCellValue("Height");
            header.createCell(5).setCellValue("Timestamp");
            header.createCell(6).setCellValue("Relative Time (ms)"); // Новый столбец для относительного времени

            // Сохраняем время старта записи
            startTimestamp = System.currentTimeMillis();

            while (running || !queue.isEmpty()) {
                try {
                    SnapshotDTO snap = queue.poll();
                    if (snap == null) {
                        Thread.sleep(10); // ждём новые данные
                        continue;
                    }

                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(snap.getX());
                    row.createCell(1).setCellValue(snap.getV());
                    row.createCell(2).setCellValue(snap.getA());
                    row.createCell(3).setCellValue(snap.getSlope());
                    row.createCell(4).setCellValue(snap.getHeight());

                    // Форматируем время
                    String formattedTime = sdf.format(new Date((long) snap.getTimestamp()));
                    row.createCell(5).setCellValue(formattedTime);

                    // Рассчитываем относительное время в миллисекундах с момента старта
                    long relativeTime = System.currentTimeMillis() - startTimestamp;
                    row.createCell(6).setCellValue(relativeTime);  // Записываем относительное время в новый столбец

                } catch (InterruptedException ignored) {}
            }

            // Сохраняем файл
            try {
                if (!outputDir.exists()) outputDir.mkdirs();
                String filename = "simulation_" + System.currentTimeMillis() + ".xlsx";
                File file = new File(outputDir, filename);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }
                workbook.dispose(); // освобождаем временные файлы
                System.out.println("Simulation exported to " + file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "ExcelExportThread");

        exportThread.setDaemon(true);
        exportThread.start();
    }

    @Override
    public void onEvent(SimulationEvent ev) {
        if (ev instanceof StateUpdateEvent su) {
            queue.offer(new SnapshotDTO(
                    su.getX(),
                    su.getV(),
                    su.getA(),
                    su.getSlope(),
                    su.getH(),
                    su.getTimestamp()
            ));
        }

        if (ev instanceof SimulationResetEvent) {
            // останавливаем поток и создаем новый файл
            stopExportThread();
            startExportThread();
        }
    }

    public void stopExportThread() {
        running = false;
        try {
            exportThread.join();
        } catch (InterruptedException ignored) {}
        running = true;
    }
}
