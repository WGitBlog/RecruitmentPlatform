package parttimejob.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parttimejob.Entity.Company;
import parttimejob.Result.R;
import parttimejob.Utils.minio.FileStorageService;
import parttimejob.service.CompanyService;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/company")
@CrossOrigin("http://localhost:5173")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private FileStorageService fileStorageService;


    @PutMapping("/updateCompany")
    public R<String> updateCompanyInfo(@RequestBody Company company){
        System.out.println(company);
        if (Objects.isNull(company)){
            R.error("传参null");
        }
        companyService.updateById(company);
        return R.success("更新company成功");
    }


    @GetMapping("/{companyId}")
    public R<Company>  getCompanyById(@PathVariable Long companyId){
        if (!Objects.isNull(companyId)){
            Company company = companyService.getCompanyById(companyId);
            return R.success(company);
        }
        return R.error("传来的参数：null");
    }



    @PostMapping("/uploadImg")
    public R<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String filePath;
        try {
            filePath = fileStorageService.uploadImgFile("CompanyImg", UUID
                    .randomUUID()+file.getOriginalFilename(), file.getInputStream());
            System.out.println(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 保存文件逻辑
        return R.success(filePath);
    }

//    TODO:实现UserId贯穿全局ThreadLocal实现之后进行更改---防止图片无线添加
//    String filePath;
//    try {
//        // 生成唯一文件名，避免覆盖
//        String uniqueFileName = userId + "_" + file.getOriginalFilename();
//
//        // 检查文件是否已存在
//        boolean exists = fileStorageService.checkFileExists("CompanyImg", uniqueFileName);
//        if (exists) {
//            // 如果文件存在，选择覆盖或删除旧文件
//            fileStorageService.deleteFile("CompanyImg", uniqueFileName);
//        }
//
//        // 上传新文件
//        filePath = fileStorageService.uploadImgFile("CompanyImg", uniqueFileName, file.getInputStream());
//        System.out.println(filePath);
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
//    // 保存文件逻辑
//    return R.success(filePath);

}
