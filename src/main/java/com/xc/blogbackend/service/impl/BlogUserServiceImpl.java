package com.xc.blogbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.blogbackend.common.ErrorCode;
import com.xc.blogbackend.contant.BlogUserConstant;
import com.xc.blogbackend.exception.BusinessException;
import com.xc.blogbackend.mapper.BlogUserMapper;
import com.xc.blogbackend.model.domain.entity.BlogUser;
import com.xc.blogbackend.model.domain.resDto.PageInfoResult;
import com.xc.blogbackend.model.domain.vo.MenuVo;
import com.xc.blogbackend.model.domain.vo.RoleVo;
import com.xc.blogbackend.model.domain.vo.UserVo;
import com.xc.blogbackend.service.BgMenuService;
import com.xc.blogbackend.service.BgRoleService;
import com.xc.blogbackend.service.BlogUserService;
import com.xc.blogbackend.utils.FieldCopyUtils;
import com.xc.blogbackend.utils.IpUtils;
import com.xc.blogbackend.utils.RandomUsernameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xc.blogbackend.contant.BlogUserConstant.USER_LOGIN_STATE;

/**
* @author XC
* @description 针对表【bg_user】的数据库操作Service实现
* @createDate 2023-11-10 18:27:39
*/
@Service
@Slf4j
public class BlogUserServiceImpl extends ServiceImpl<BlogUserMapper, BlogUser>
    implements BlogUserService {

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private BgMenuService bgMenuService;

    @Resource
    private BgRoleService bgRoleService;

    @Override
    public UserVo userLogin(String username, String password, String ip, HttpServletRequest request){
        //1.校验
        //账户密码不能为空
        if (StringUtils.isAnyBlank(username,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户密码不能为空");
        }
        //账户不小于4
        if (username.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不小于4位");
        }
        //密码不小于8
        if (password.length() <8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不小于8位");
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不能包含特殊字符");
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((BlogUserConstant.SALT + password).getBytes());
        //查询用户是否存在
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BINARY username",username);
        queryWrapper.eq("BINARY password",encryptPassword);
        BlogUser user = blogUserMapper.selectOne(queryWrapper);
        //用户不存在
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        //更新插入ip
        UpdateWrapper<BlogUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.getId()).set("ip", ip);
        this.update(updateWrapper);

        //获取用户菜单
        List<MenuVo> menuVos = bgMenuService.roleQueryMenus(user.getId());
        List<RoleVo> userRoles = bgRoleService.getUserRole(user.getId());
        List<String> roles = userRoles.stream().map(RoleVo::getCode).collect(Collectors.toList());

        UserVo loginResDto = FieldCopyUtils.copyProperties(user, UserVo.class);
        loginResDto.setMenus(menuVos);
        loginResDto.setRoles(roles);

        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,loginResDto);

        return loginResDto;
    }

    @Override
    public Map<String,String> userRegister(String username, String password, String checkPassword, String ip) {
        //1.校验
        //账户密码不能为空
        if (StringUtils.isAnyBlank(username,password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //账户不小于4
        if (username.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户大于4位");
        }
        //密码不小于8
        if (password.length() <8 || checkPassword.length() <8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码小于8位");
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不能包含特殊字符");
        }
        //密码和校验密码相同
        if (!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和校验密码不相同");
        }
        //账户不能重复
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        long count = blogUserMapper.selectCount(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户不能重复");
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((BlogUserConstant.SALT + password).getBytes());
        //3.插入数据
        BlogUser blogUser = new BlogUser();
        blogUser.setUsername(username);
        blogUser.setPassword(encryptPassword);
        blogUser.setAvatar("https://pic.imgdb.cn/item/65114060c458853aef1f9fa4.jpg");
        blogUser.setNickName(RandomUsernameGenerator.generateRandomUsername());
        blogUser.setIp(ip);
        boolean saveResult = this.save(blogUser);
        if(!saveResult){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"为空");
        }
        Map<String, String> userMap = new HashMap<>();
        userMap.put("id",String.valueOf(blogUser.getId()));
        userMap.put("username",blogUser.getUsername());
        return userMap;
    }

    @Override
    public UserVo getOneUserInfo(Long userId) {
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"为空");
        }
        queryWrapper.eq("id",userId);
        BlogUser blogUser = blogUserMapper.selectOne(queryWrapper);
        UserVo userVo = FieldCopyUtils.copyProperties(blogUser, UserVo.class);
        return userVo;
    }

    @Override
    public PageInfoResult<BlogUser> getUserList(Integer current, String nickName, Integer role, Integer size) {

        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        if (role != null) {
            queryWrapper.eq("role", role);
        }
        if (nickName != null && !nickName.isEmpty()) {
            queryWrapper.like("nick_name", "%" + nickName + "%");
        }
        //构建查询条件并返回除密码外的其他列
        queryWrapper.select(BlogUser.class, info -> !info.getColumn().equals("password"));

        // 创建Page对象，设置当前页和分页大小
        Page<BlogUser> page = new Page<>(current,size);
        // 获取用户列表，使用page方法传入Page对象和QueryWrapper对象
        Page<BlogUser> userPage = blogUserMapper.selectPage(page, queryWrapper);
        // 获取分页数据
        List<BlogUser> rows = userPage.getRecords();
        // 获取说说总数
        long count = userPage.getTotal();

        rows.forEach(row -> {
            if (row.getIp() != null && !row.getIp().isEmpty()) {
                row.setIpAddress(IpUtils.getLocation(row.getIp()));
            } else {
                row.setIpAddress("火星");
            }
        });

        PageInfoResult<BlogUser> pageInfoResult = new PageInfoResult<>();
        pageInfoResult.setList(rows);
        pageInfoResult.setTotal(count);
        pageInfoResult.setCurrent(current);
        pageInfoResult.setSize(size);

        return pageInfoResult;
    }

    @Override
    public Long getUserCount() {
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        Long count = blogUserMapper.selectCount(queryWrapper);

        return count;
    }

    @Override
    public String getAuthorNameById(Long userId) {
        BlogUser blogUser = blogUserMapper.selectById(userId);

        return blogUser != null ? blogUser.getNickName() : null;
    }

    @Override
    public Boolean updateOwnUserInfo(Map<String, Object> request) {
        Long id = (Long) request.get("id");
        String avatar = (String) request.get("avatar");
        String nickName = (String) request.get("nick_name");
        String qq = (String) request.get("qq");

        BlogUser blogUser = new BlogUser();
        blogUser.setId(id);
        blogUser.setAvatar(avatar);
        blogUser.setNickName(nickName);
        blogUser.setQq(qq);

        int i = blogUserMapper.updateById(blogUser);

        return i > 0;
    }

    @Override
    public Boolean updatePassword(Long id, String password,String password1) {
        //1.校验
        //密码不能为空
        if (StringUtils.isAnyBlank(password1)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不能为空");
        }
        //密码不小于8
        if (password1.length() <8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不小于8位");
        }
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((BlogUserConstant.SALT + password).getBytes());
        BlogUser blogUser = blogUserMapper.selectById(id);
        String oldPassword = blogUser.getPassword();
        if (oldPassword.equals(encryptPassword)){
            String newPassword =  DigestUtils.md5DigestAsHex((BlogUserConstant.SALT + password1).getBytes());
            blogUser.setPassword(newPassword);
            int i = blogUserMapper.updateById(blogUser);
            return i > 0;
        }else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public Boolean updateRole(Long id, Integer role) {
//        BlogUser blogUser = new BlogUser();
//        blogUser.setRole(role);
//        UpdateWrapper<BlogUser> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("id",id);
//        int update = blogUserMapper.update(blogUser, updateWrapper);
//
//        return update > 0;
        return false;
    }
}




