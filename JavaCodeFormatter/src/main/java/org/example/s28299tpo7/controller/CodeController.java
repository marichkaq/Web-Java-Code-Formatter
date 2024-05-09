package org.example.s28299tpo7.controller;

import org.example.s28299tpo7.model.CodeSnippet;
import org.example.s28299tpo7.service.CodeFormatter;
import org.example.s28299tpo7.service.CodeSnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;


@Controller
public class CodeController {
    private final CodeFormatter codeFormatter;
    private final CodeSnippetService codeSnippetService;

    @Autowired
    public CodeController(CodeFormatter codeFormatter, CodeSnippetService codeSnippetService){
        this.codeFormatter = codeFormatter;
        this.codeSnippetService = codeSnippetService;
    }

    @GetMapping("/format")
    public String showForm(Model model){
        return "format";
    }

    @PostMapping("format")
    public String formatCode(@RequestParam("origCode") String origCode, Model model){
        model.addAttribute("origCode", origCode);
        try {
            String formattedCode = codeFormatter.formatCode(origCode);
            CodeSnippet codeSnippet = new CodeSnippet();
            codeSnippet.setFormattedCode(formattedCode);
            model.addAttribute("codeSnippet", codeSnippet);
            return "format";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error in formatting code: " + e.getMessage());
            return "format";
        }
    }

    @PostMapping("/save")
    public String saveFormattedCode(@RequestParam("formattedCode") String formattedCode,
                                    @RequestParam("duration") Long duration, RedirectAttributes redirectAttributes) {
        try {
            CodeSnippet codeSnippet = new CodeSnippet(formattedCode, duration);
            codeSnippetService.saveCodeSnippet(codeSnippet);
            redirectAttributes.addFlashAttribute("successMessage", "Code snippet saved successfully with ID: " + codeSnippet.getId());
            return "redirect:/view/" + codeSnippet.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save code snippet: " + e.getMessage());
            return "redirect:/format";
        }
    }

    @GetMapping("/view/{id}")
    public String viewSavedCode(@PathVariable String id, Model model){
        try{
            CodeSnippet codeSnippet = codeSnippetService.loadCodeSnippet(id);
            if(codeSnippet == null){
                model.addAttribute("errorMessage", "No code snippet found with ID: " + id);
                return "view";
            }
            model.addAttribute("codeSnippet", codeSnippet);
            model.addAttribute("id", id);
            return "view";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving the formatted code: " + e.getMessage());
            return "view";
        }
    }

}
