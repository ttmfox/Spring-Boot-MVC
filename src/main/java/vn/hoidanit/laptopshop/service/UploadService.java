package vn.hoidanit.laptopshop.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext) {       
        this.servletContext = servletContext;
    }

    public String handleSaveFile(MultipartFile file, String targetFile){
        String finalName = "";
        String rootPath = this.servletContext.getRealPath("/resources/images");
        try {
            byte[] bytes = file.getBytes();

            
            File dir = new File(rootPath + File.separator + targetFile);
            if (!dir.exists())
                dir.mkdirs();
            // Create the file on server
            finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File serverFile = new File(dir.getAbsolutePath() + File.separator + finalName);
                    
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return finalName;
    }
}
