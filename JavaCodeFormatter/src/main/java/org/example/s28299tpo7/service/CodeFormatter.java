package org.example.s28299tpo7.service;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import org.springframework.stereotype.Service;

@Service
public class CodeFormatter {
    public String formatCode(String sourceCode) throws FormatterException{
        if (sourceCode == null){
            throw new FormatterException("Source code is null");
        }
        Formatter formatter = new Formatter();
        return formatter.formatSource(sourceCode);
    }
}
