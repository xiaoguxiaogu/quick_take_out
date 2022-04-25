package com.me.quick.filter;

import ch.qos.logback.classic.turbo.TurboFilter;
import com.alibaba.fastjson.JSON;
import com.me.quick.common.BaseContext;
import com.me.quick.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 * @author chen
 * @Date 2022-04-15-16:54
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String requestURI = request.getRequestURI();
       // log.info("拦截到请求:{}",requestURI);
        //定义不需拦截的url
        String[] urls={
          "/employee/login",
          "employee/logout",
          "/backend/**",//静态资源可以访问，只要看不到数据即可
          "/front/**",
          "/common/**",
          "/user/sendMsg",
                "/user/login"
        };
        boolean match=match(urls,requestURI);
        if(match){
            //log.info("本次请求不需要处理：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("employee")!=null){
          //  log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
           // log.info("线程id为：{}",id);
            filterChain.doFilter(request,response);
            return;
        }
        //移动端判断
        if(request.getSession().getAttribute("user")!=null){
            //  log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            long id = Thread.currentThread().getId();
            // log.info("线程id为：{}",id);
            filterChain.doFilter(request,response);
            return;
        }
        //如果未登录
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean match(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
