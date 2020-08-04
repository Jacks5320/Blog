package com.jk.blog.controller.admin;

import com.jk.blog.pojo.Blog;
import com.jk.blog.pojo.User;
import com.jk.blog.service.BlogService;
import com.jk.blog.service.TagService;
import com.jk.blog.service.TypeService;
import com.jk.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 分页查询
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog,
                       Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;//"admin/blogs"
    }

    /**
     * 查询页面
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String searchList(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                             BlogQuery blog,
                             Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    /**
     * 跳转到博客编辑
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String blogInput(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
        return INPUT;//admin/blogs-input
    }

    /**
     * 修改博客
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String blogUpdate(@PathVariable Long id, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;//admin/blogs-input
    }

    /**
     * 保存博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String postBlog(Blog blog, RedirectAttributes attributes,
                           HttpSession session){
        blog.setUser((User) session.getAttribute("user"));

        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        System.out.println(blog.toString());

        Blog b;
        if (blog.getId() == null){
            b = blogService.saveBlog(blog);
        }else {
            b = blogService.updateBlog(blog.getId(),blog);
        }

        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }

        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }
}
