// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.filedelimited.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.common.EncodingTypeProperties;
import org.talend.components.common.runtime.FileRuntimeHelper;
import org.talend.components.filedelimited.FileDelimitedProperties;
import org.talend.components.filedelimited.tfileinputdelimited.TFileInputDelimitedProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.fileprocess.FileInputDelimited;

import com.google.gson.Gson;
import com.talend.csv.CSVReader;

public class FileInputDelimitedRuntime {

    private transient static final Logger LOG = LoggerFactory.getLogger(FileInputDelimitedRuntime.class);

    private FileDelimitedProperties props;

    protected Object fileNameOrStream;

    private ZipInputStream zipInputStream;

    private String encoding;

    private int nbRandom = -1;

    private int header;

    private int footer;

    protected int limit;

    private boolean sourceIsStream;

    private char fieldSeparator;

    private char[] rowSeparator;

    private char escapeChar;

    private char textEnclosureChar;

    protected int totalLine = 0;

    protected int lastLine = -1;

    protected int currentLine;

    protected boolean schemaIsDynamic;

    private boolean uncompress;

    private boolean splitRecord;

    private boolean random;

    // For preview data
    protected List<String> columnNames;

    protected List<Integer> columnsLength;

    public FileInputDelimitedRuntime(FileDelimitedProperties props) {
        this.props = props;
    }

    public void init() throws IOException {
        fileNameOrStream = props.fileName.getValue();

        sourceIsStream = fileNameOrStream instanceof InputStream;

        encoding = getEncoding();

        schemaIsDynamic = AvroUtils.isIncludeAllFields(props.main.schema.getValue());

        header = (props.header.getValue() == null) ? -1 : props.header.getValue();

        if (schemaIsDynamic) {
            header = header - 1;
        }
        if (props instanceof TFileInputDelimitedProperties) {
            uncompress = ((TFileInputDelimitedProperties) props).uncompress.getValue();
            splitRecord = ((TFileInputDelimitedProperties) props).splitRecord.getValue();
            if (((TFileInputDelimitedProperties) props).random.getValue()) {
                nbRandom = (((TFileInputDelimitedProperties) props).nbRandom.getValue() == null || uncompress) ? -1
                        : ((TFileInputDelimitedProperties) props).nbRandom.getValue();
            }
        }
        footer = (props.footer.getValue() == null || uncompress) ? -1 : props.footer.getValue();
        limit = (props.limit.getValue() == null) ? -1 : props.limit.getValue();

        if (sourceIsStream) {
            zipInputStream = FileRuntimeHelper.getZipInputStream((InputStream) fileNameOrStream);
        } else {
            zipInputStream = FileRuntimeHelper.getZipInputStream(String.valueOf(fileNameOrStream));
        }

        fieldSeparator = getFieldSeparator();
        rowSeparator = getRowSeparator();

        setEscapeAndTextEnclosure(props.escapeChar.getValue(), props.textEnclosure.getValue());
    }

    public FileInputDelimited getFileDelimited() throws IOException {
        FileInputDelimited fileInputDelimited = null;
        if (uncompress) {
            ZipEntry zipEntry = null;
            if (hashNextEntry()) {
                fileInputDelimited = new FileInputDelimited(zipInputStream, encoding, props.fieldSeparator.getValue(),
                        props.rowSeparator.getValue(), props.removeEmptyRow.getValue(), header, footer, limit, nbRandom,
                        splitRecord);
            }
        } else {
            if (sourceIsStream) {
                checkFooterAndRandom();
                fileInputDelimited = new FileInputDelimited((InputStream) fileNameOrStream, encoding,
                        props.fieldSeparator.getValue(), props.rowSeparator.getValue(), props.removeEmptyRow.getValue(), header,
                        footer, limit, nbRandom, splitRecord);
            } else {
                fileInputDelimited = new FileInputDelimited(String.valueOf(fileNameOrStream), encoding,
                        props.fieldSeparator.getValue(), props.rowSeparator.getValue(), props.removeEmptyRow.getValue(), header,
                        footer, limit, nbRandom, splitRecord);
            }

        }

        return fileInputDelimited;
    }

    public CSVReader getCsvReader() throws IOException {

        String[] row = null;
        currentLine = 0;
        CSVReader csvReader = null;

        if (uncompress) {
            if (hashNextEntry()) {
                csvReader = new CSVReader(zipInputStream, getFieldSeparator(), encoding);
            } else {
                return null;
            }
        } else {
            if (sourceIsStream) {
                checkFooter();
                csvReader = new CSVReader((java.io.InputStream) fileNameOrStream, fieldSeparator, encoding);
            } else {
                csvReader = new CSVReader(new java.io.BufferedReader(
                        new java.io.InputStreamReader(new java.io.FileInputStream(String.valueOf(fileNameOrStream)), encoding)),
                        fieldSeparator);
            }
        }

        csvReader.setTrimWhitespace(false);
        if ((rowSeparator[0] != '\n') && (rowSeparator[0] != '\r')) {
            csvReader.setLineEnd("" + rowSeparator[0]);
        }
        csvReader.setQuoteChar(textEnclosureChar);
        csvReader.setEscapeChar(escapeChar);
        if (footer > 0) {
            for (totalLine = 0; totalLine < header; totalLine++) {
                csvReader.readNext();
            }
            // TODO check csv option setting
            csvReader.setSkipEmptyRecords(props.removeEmptyRow.getValue());
            while (csvReader.readNext()) {

                row = csvReader.getValues();
                // empty line when row separator is '\n'
                if (!(row.length == 1 && ("\015").equals(row[0]))) {
                    totalLine++;
                }
            }
            int lastLineTemp = totalLine - footer < 0 ? 0 : totalLine - footer;
            if (lastLine > 0) {
                lastLine = lastLine < lastLineTemp ? lastLine : lastLineTemp;
            } else {
                lastLine = lastLineTemp;
            }
            csvReader.close();

            if (sourceIsStream) {
                csvReader = new CSVReader((InputStream) fileNameOrStream, fieldSeparator, encoding);
            } else {
                csvReader = new CSVReader(new java.io.BufferedReader(
                        new java.io.InputStreamReader(new java.io.FileInputStream(String.valueOf(fileNameOrStream)), encoding)),
                        fieldSeparator);
            }
            csvReader.setTrimWhitespace(false);
            if ((rowSeparator[0] != '\n') && (rowSeparator[0] != '\r')) {
                csvReader.setLineEnd("" + rowSeparator[0]);
            }
            csvReader.setQuoteChar(textEnclosureChar);
            csvReader.setEscapeChar(escapeChar);
        }
        if (limit != 0) {
            for (currentLine = 0; currentLine < header; currentLine++) {
                csvReader.readNext();
            }
        }
        csvReader.setSkipEmptyRecords(props.removeEmptyRow.getValue());
        return csvReader;
    }

    private char getFieldSeparator() {
        String fieldSeparator = props.fieldSeparator.getValue();
        char fSeparator;
        if (fieldSeparator != null && fieldSeparator.length() > 0) {
            fSeparator = fieldSeparator.toCharArray()[0];
        } else {
            throw new IllegalArgumentException("Field Separator must be assigned a char.");
        }

        return fSeparator;
    }

    private char[] getRowSeparator() {
        String rowSeparator = props.rowSeparator.getValue();
        if (rowSeparator != null && rowSeparator.length() > 0) {
            return rowSeparator.toCharArray();
        } else {
            throw new IllegalArgumentException("Row Separator must be assigned a char.");
        }
    }

    // Setup the escape char and text Enclosure char, Because of need consider the migration so little complex
    private void setEscapeAndTextEnclosure(String escape, String textEnclosure) {
        // TODO add more junit for this and more detail comments
        if (StringUtils.isEmpty(textEnclosure) || "\"".equals(textEnclosure)) {
            textEnclosureChar = '\"';
        } else {
            textEnclosureChar = textEnclosure.charAt(0);
        }

        if (escape != null && "\\".equals(escape)) {
            escapeChar = '\\';
        } else if (escape.equals(textEnclosure)) {
            escapeChar = textEnclosureChar;
        } else {
            if (!StringUtils.isEmpty(escape)) {
                escapeChar = escape.charAt(0);
            } else {
                throw new IllegalArgumentException("Escape Char must be assigned a char.");
            }
        }

    }

    private String getEncoding() {
        if (EncodingTypeProperties.ENCODING_TYPE_CUSTOM.equals(props.encoding.encodingType.getValue())) {
            return props.encoding.customEncoding.getValue();
        }
        return props.encoding.encodingType.getValue();
    }

    protected boolean hashNextEntry() throws IOException {
        if (zipInputStream == null) {
            return false;
        }
        ZipEntry zipEntry = null;
        while (true) {
            zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null) {
                return false;
            } else if (zipEntry.isDirectory()) {
                continue;
            }
            return true;
        }
    }

    private void checkFooterAndRandom() throws IOException {

        if (footer > 0 || nbRandom > 0) {
            throw new IOException("When the input source is a stream,footer and random shouldn't be bigger than 0.");
        }
    }

    public void checkFooter() throws IOException {
        if (footer > 0) {
            throw new IOException("When the input source is a stream,footer shouldn't be bigger than 0.");
        }
    }

    // Preview data and guess the columns
    public String previewData(int maxRowsToPreview) throws IOException {
        init();
        Map<String, Object> result = new HashMap<String, Object>();
        boolean retrieveHeader = false;
        if (header > 0) {
            header = header - 1;
            retrieveHeader = true;
        }
        String[] rowData = null;
        List<String[]> data = new ArrayList<>();
        if (props.csvOptions.getValue()) {
            if (limit < 1) {
                limit = maxRowsToPreview;
            }
            CSVReader csvReader = getCsvReader();
            if (retrieveHeader) {
                lastLine = lastLine - 1;
            }
            try {
                if (csvReader != null && csvReader.readNext()) {
                    rowData = csvReader.getValues();
                    if (retrieveHeader) {
                        result.put("columnNames", rowData);
                        columnNames = Arrays.asList(rowData);
                        LOG.debug("columnNames " + columnNames);
                    } else {
                        data.add(rowData);
                        updateColumnsLength(rowData);
                    }
                    while (csvReader.readNext()) {
                        rowData = csvReader.getValues();
                        if (props.removeEmptyRow.getValue() && (rowData.length == 1 && ("\015").equals(rowData[0]))) {
                            continue;
                        }
                        currentLine++;
                        if (lastLine > -1 && (currentLine > lastLine || currentLine > maxRowsToPreview)) {
                            break;
                        }
                        data.add(rowData);
                        updateColumnsLength(rowData);
                        LOG.debug("Preview row " + currentLine + " : " + Arrays.asList(rowData));
                    }
                }
            } finally {
                if (csvReader != null) {
                    csvReader.close();
                }
            }
        } else {
            if (retrieveHeader) {
                if (limit > 0) {
                    limit = limit + 1;
                } else {
                    if (limit < 1) {
                        limit = maxRowsToPreview + 1;
                    }
                }
            }
            FileInputDelimited fid = getFileDelimited();
            try {
                while (fid != null && fid.nextRecord()) {
                    int currentRowColsCount = fid.getColumnsCountOfCurrentRow();
                    rowData = new String[currentRowColsCount];
                    for (int i = 0; i < rowData.length; i++) {
                        rowData[i] = fid.get(i);
                    }
                    if (retrieveHeader) {
                        result.put("columnNames", rowData);
                        columnNames = Arrays.asList(rowData);
                        LOG.debug("columnNames " + columnNames);
                        retrieveHeader = false;
                    } else {
                        currentLine++;
                        data.add(rowData);
                        updateColumnsLength(rowData);
                        LOG.debug("Preview row " + currentLine + " : " + Arrays.asList(rowData));
                    }
                }
            } finally {
                if (fid != null) {
                    fid.close();
                }
            }
        }
        result.put("data", data);
        if (data.size() > 0) {
            LOG.debug("Max columns count:" + columnsLength.size());
        }
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    // Get the column length from the preview data
    private void updateColumnsLength(String[] rowData) {
        if (columnsLength == null) {
            columnsLength = new ArrayList<>();
        }
        int currentColumnsCount = columnsLength.size();
        if (rowData != null) {
            for (int i = 0; i < rowData.length; i++) {
                if (i >= currentColumnsCount) {
                    if (rowData[i] != null) {
                        columnsLength.add(rowData[i].length());
                    } else {
                        columnsLength.add(0);
                    }
                } else {
                    if (rowData[i] != null && rowData[i].length() > columnsLength.get(i)) {
                        columnsLength.set(i, rowData[i].length());
                    }
                }
            }
        }
    }

}
