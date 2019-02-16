package com.delicacy.controller;

import com.delicacy.bean.CatalogBean;
import com.delicacy.bean.ResponseBean;
import com.delicacy.domain.Catalog;
import com.delicacy.domain.User;
import com.delicacy.service.CatalogService;
import com.delicacy.util.ConstraintViolationExceptionHandler;
import com.delicacy.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

/**
 * @Author: MyDear
 */
@Controller
@RequestMapping("/catalogs")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public String listCatalogs(@RequestParam(value = "username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogList = catalogService.listCatalogs(user);

        User principal = UserUtil.isOwner();
        if (Objects.nonNull(principal) && user.getId() == principal.getId()) {
            model.addAttribute("isCatalogsOwner", true);
        } else {
            model.addAttribute("isCatalogsOwner", false);
        }
        model.addAttribute("catalogs", catalogList);
        return "userspace/u :: #catalogReplace";
    }

    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogBean.username)")
    public ResponseEntity<ResponseBean> createCatalogs(@RequestBody CatalogBean catalogBean) {
        String username = catalogBean.getUsername();
        User user = (User) userDetailsService.loadUserByUsername(username);
        Catalog catalog = catalogBean.getCatalog();
        catalog.setUser(user);
        try {
            catalogService.saveCatalog(catalog);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true, "发表分类成功！", StringUtils.EMPTY));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<ResponseBean> delete(String username, @PathVariable("id") int id) {
        try {
            catalogService.removeCatalog(id);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(ResponseBean.of(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseBean.of(true, "删除分类成功！", StringUtils.EMPTY));
    }

    @GetMapping("/edit")
    public String getCatalogEdit(Model model) {
        model.addAttribute("catalog", new Catalog());
        return "userspace/catalog_edit";
    }

    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") int id, Model model) {
        Catalog catalog = catalogService.getCatalogById(id);
        model.addAttribute("catalog", catalog);
        return "userspace/catalog_edit";
    }
}
