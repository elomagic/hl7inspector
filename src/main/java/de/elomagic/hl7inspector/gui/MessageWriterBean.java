/*
 * Copyright 2006 Carsten Rambow
 *
 * Licensed under the GNU Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/gpl.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.io.Frame;

import java.io.File;

/**
 *
 * @author rambow
 */
public class MessageWriterBean {
    private Frame frame = new Frame();
    private File destinationFolder = new File(".");
    private boolean OnlySelectedFiles = false;
    private boolean manyFiles = true;
    private String dataFilePrefix = "";
    private String dataFileExtension = "hl7";
    private String semaphoreExtension = "sem";
    private boolean generateSempahore = true;
    private File singleFileName = null;
    private String encoding = "ISO-8859-1";

    /**
     * Creates a new instance of MessageWriterBean.
     */
    public MessageWriterBean() {
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public void setDestinationFolder(File destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public boolean isOnlySelectedFiles() {
        return OnlySelectedFiles;
    }

    public void setOnlySelectedFiles(boolean OnlySelectedFiles) {
        this.OnlySelectedFiles = OnlySelectedFiles;
    }

    public boolean isManyFiles() {
        return manyFiles;
    }

    public void setManyFiles(boolean manyFiles) {
        this.manyFiles = manyFiles;
    }

    public String getDataFilePrefix() {
        return dataFilePrefix;
    }

    public void setDataFilePrefix(String dataFilePrefix) {
        this.dataFilePrefix = dataFilePrefix;
    }

    public String getDataFileExtension() {
        return dataFileExtension;
    }

    public void setDataFileExtension(String dataFileExtension) {
        this.dataFileExtension = dataFileExtension;
    }

    public String getSemaphoreExtension() {
        return semaphoreExtension;
    }

    public void setSemaphoreExtension(String semaphoreExtension) {
        this.semaphoreExtension = semaphoreExtension;
    }

    public boolean isGenerateSempahore() {
        return generateSempahore;
    }

    public void setGenerateSempahore(boolean generateSempahore) {
        this.generateSempahore = generateSempahore;
    }

    public File getSingleFileName() {
        return singleFileName;
    }

    public void setSingleFileName(File singleFileName) {
        this.singleFileName = singleFileName;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
