package com.example.asm.util;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.service.HoaDonService;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.lowagie.text.DocumentException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PDFGenerator {
    private final HoaDonService hoaDonService;

    public PDFGenerator(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }


}
