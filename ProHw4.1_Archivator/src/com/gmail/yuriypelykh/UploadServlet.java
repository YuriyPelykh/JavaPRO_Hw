package com.gmail.yuriypelykh;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadServlet extends HttpServlet {

    private Random random = new Random();
    static final String template = "<html>" +
            "<head><title>Archivator</title></head>" +
            "<body>%s</body></html>";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String repDir = makeDirectory("Temp");
        String uplDir = makeDirectory("Upload");
        String archiveName = "yourzip";

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024);
        factory.setRepository(new File(repDir));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1024 * 1024 * 10);
        Map <String,String> filesMap = new HashMap<>();
        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    if (item.getString().equals("")){
                    } else {
                    archiveName = item.getString();
                    }
                } else {
                    String uploadedFileName = processUploadedFile(item, uplDir);
                    filesMap.put(uploadedFileName,item.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        String zipName = archiveUploadedFiles(filesMap,archiveName,uplDir);
        String msg = "<h2>Link to archive:</h2><br>" +
                "<a href=\"http://localhost:8086/Upload/"+ zipName + "\">Donwload</a>";
        response.getWriter().println(String.format(template, msg));
    }

    private String makeDirectory(String dirName){
        //File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        String tempDir = getServletContext().getRealPath(".");
        String dirPath = tempDir + "/" + dirName;
        File makeDir = new File(dirPath);
        makeDir.mkdir();
        return makeDir.getAbsolutePath();
    }

    private String processUploadedFile(FileItem item, String uploadPath) throws Exception {
        File uploadedFile = null;
        String path = "";
        if (item.getSize() != 0) {
            do {
                path = uploadPath + "/" + random.nextInt() + item.getName();
                uploadedFile = new File(path);
            } while (uploadedFile.exists());
            uploadedFile.createNewFile();
            item.write(uploadedFile);
        }
        return path;
    }

    public String archiveUploadedFiles(Map filesMap, String zipName, String uplDir) throws IOException {
        String archName = "";
        File newZip = null;
        do {
            archName = zipName + random.nextInt() + ".zip";
            newZip = new File(uplDir + "/" + archName);
        } while (newZip.exists());
        
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(newZip.getAbsolutePath()));
        Set<Map.Entry<String,String>> mapSet = filesMap.entrySet();
        for (Map.Entry<String,String> file: mapSet) {
            try(FileInputStream fis= new FileInputStream(file.getKey())){
                ZipEntry entry=new ZipEntry(file.getValue());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
            }catch(Exception ex){
                System.out.println(ex.getMessage());
            }finally {
                File f = new File(file.getKey());
                f.delete();
            }
        }
        zout.close();
        return archName;
    }
}