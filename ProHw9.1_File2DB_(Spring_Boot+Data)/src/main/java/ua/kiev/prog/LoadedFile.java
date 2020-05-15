package ua.kiev.prog;

import javax.persistence.*;

@Entity
@Table(name="files")
public class LoadedFile {
    @Id
    @GeneratedValue
    private long id;

    private String filename;

    @Lob
    @Column(name = "filedata", columnDefinition="BLOB")

    private byte[] filedata;
    private double filesize;

    public LoadedFile() {}

    public LoadedFile(String filename, byte[] filedata) {
        this.filename = filename;
        this.filedata = filedata;
        this.filesize = filedata.length/1000.0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFiledata() {
        return filedata;
    }

    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }

    public double getFilesize() {
        return filesize;
    }

    public void setFilesize(double filesize) {
        this.filesize = filesize;
    }
}
