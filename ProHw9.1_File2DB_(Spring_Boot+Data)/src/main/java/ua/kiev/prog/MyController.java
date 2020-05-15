package ua.kiev.prog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class MyController {
    static final int ITEMS_PER_PAGE = 5;

    //@Autowired
    private final FileService fileService;

    public MyController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(required = false,
                                defaultValue = "0") Integer page) {
        if (page < 0) page = 0;

        List<LoadedFile> files = fileService
                .findAll(new PageRequest(page,
                        ITEMS_PER_PAGE,
                        Sort.Direction.DESC, "id"));

        model.addAttribute("files", files);
        model.addAttribute("allPages", getPageCount());

        return "index";
    }

    @RequestMapping("/file_upload_page")
    public String fileUploadPage(Model model) {
        return "file_upload_page";
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam String pattern, Model model) {
        model.addAttribute("files", fileService.findByPattern(pattern, null));

        return "index";
    }

    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> delete(@RequestParam(value = "toDelete[]", required = false) long[] toDelete) {
        if (toDelete != null && toDelete.length > 0)
            fileService.deleteFiles(toDelete);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public String fileAdd(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){

        try {
            String filename = file.getOriginalFilename();
            byte[] filedata = file.getBytes();
            LoadedFile loadedFile = new LoadedFile(filename, filedata);
            fileService.addFile(loadedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    private long getPageCount() {
        long totalCount = fileService.count();
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }

}
