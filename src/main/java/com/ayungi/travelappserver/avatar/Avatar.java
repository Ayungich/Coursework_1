package com.ayungi.travelappserver.avatar;

import jakarta.persistence.*;

@Entity
@Table(name = "avatars")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}

    public String getFileType() {return fileType;}
    public void setFileType(String fileType) {this.fileType = fileType;}

    public byte[] getData() {return data;}
    public void setData(byte[] data) {this.data = data;}
}
