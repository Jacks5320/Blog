package com.jk.blog.controller.admin;

import com.jk.blog.pojo.Tag;
import com.jk.blog.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 分页查询
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String list(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        //TODO 分页查询
        model.addAttribute("page", tagService.listTag(pageable));//一页数据
        return "admin/tags";
    }

    /**
     * 跳转到编辑页面
     *
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model) {
        //TODO 跳转到标签编辑页面
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /**
     * 保存新标签
     *
     * @param tag
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String saveT(Tag tag,
                        BindingResult result,
                        RedirectAttributes attributes) {
        //TODO 保存新增标签
        if (tag.getName().equals("")) {
            // attributes.addFlashAttribute("message", "操作失败,标签不能为空");
            result.rejectValue("name", "nameError", "标签名称不能为空");
            return "admin/tags-input";
        } else {
            if (tagService.getTagByName(tag.getName()) != null) {
                result.rejectValue("name", "nameError", "该分类已存在");
                return "admin/tags-input";
            } else {
                tagService.saveTag(tag);
                attributes.addFlashAttribute("message", "保存成功");
            }
        }

        return "redirect:/admin/tags";
    }

    /**
     * 跳转到修改标签页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        //TODO 修改标签名称
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 修改标签
     *
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String tagUpdate(Tag tag,
                              BindingResult result,
                              @PathVariable Long id,
                              RedirectAttributes attributes) {
        //TODO 更新标签
        if (tag.getName().equals("")) {
            result.rejectValue("name", "nameError", "标签名称不能为空");
            return "admin/tags-input";
        } else {
            if (tagService.getTagByName(tag.getName()) != null) {
                result.rejectValue("name", "nameError", "该标签已存在");
                return "admin/tags-input";
            } else {
                tagService.updateTag(id, tag);
                attributes.addFlashAttribute("message", "更新成功");
            }
        }

        return "redirect:/admin/tags";
    }

    /**
     * 删除标签
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteTags(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}
