package org.zerock.moamoa.controller;


import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.service.MoamoaService;
import org.zerock.moamoa.domain.entity.User;

@RequiredArgsConstructor
@Controller
public class MoamoaController {

    private final MoamoaService moamoaService;

    @RequestMapping("/todo")
    public String list(Model model){
        List<User> userList = this.moamoaService.getList();
        model.addAttribute("userList",userList);
        return "todolist";
    }

    @RequestMapping("/")
    public String root(){
        return "redirect:/todo";
    }

    @PostMapping("/todo/create")
    public String userCreate(@RequestParam String content){
        this.moamoaService.create(content);
        return "redirect:/todo";
    }

    // 삭제 기능
    @DeleteMapping("/todo/delete/{id}")
    public String userDelete(@PathVariable Integer id){
        this.moamoaService.delete(id);
        return "redirect:/todo";
    }

    @PutMapping("/todo/update/{id}")
    public String userUpdate(@RequestBody String content, @PathVariable Integer id){
        this.moamoaService.update(id, content);
        return "redirect:/todo";
    }
}