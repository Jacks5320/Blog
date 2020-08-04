package com.jk.blog.controller.admin;

import com.jk.blog.pojo.Type;
import com.jk.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 分页查询
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String list(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        //TODO 分页查询
        model.addAttribute("page", typeService.listType(pageable));//一页数据
        return "admin/types";
    }

    /**
     * 跳转到编辑页面
     *
     * @param model
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        //TODO 跳转到分类编辑页面
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 保存新分类
     *
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String saveT(Type type,
                        BindingResult result,
                        RedirectAttributes attributes) {
        //TODO 保存新增分类
        if (type.getName().equals("")) {
            // attributes.addFlashAttribute("message", "操作失败,标签不能为空");
            result.rejectValue("name", "nameError", "分类名称不能为空");
            return "admin/types-input";
        } else {
            if (typeService.findByName(type.getName()) != null) {
                result.rejectValue("name", "nameError", "该分类已存在");
                return "admin/types-input";
            } else {
                typeService.saveType(type);
                attributes.addFlashAttribute("message", "保存成功");
            }
        }

        return "redirect:/admin/types";
    }

    /**
     * 跳转到修改分类页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 修改分类名称
     *
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String typesUpdate(Type type,
                              BindingResult result,
                              @PathVariable Long id,
                              RedirectAttributes attributes) {
        //TODO 修改分类名称
        if (type.getName().equals("")) {
            result.rejectValue("name", "nameError", "分类名称不能为空");
            return "admin/types-input";
        } else {
            if (typeService.findByName(type.getName()) != null) {
                result.rejectValue("name", "nameError", "该分类已存在");
                return "admin/types-input";
            } else {
                typeService.updateType(id, type);
                attributes.addFlashAttribute("message", "更新成功");
            }
        }

        return "redirect:/admin/types";
    }

    /**
     * 删除分类
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteTypes(@PathVariable Long id,RedirectAttributes attributes){
        //TODO 删除分类
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }

}
