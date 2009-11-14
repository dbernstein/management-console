
package org.duracloud.duradmin.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.duracloud.duradmin.domain.Tag;
import org.duracloud.duradmin.util.TagUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class RemoveTagController
        extends TagController {
    
    protected final Log log = LogFactory.getLog(getClass());

    @Override
    protected ModelAndView handleTag(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Tag tag,
                                              BindException errors) throws Exception{
        Map<String,String> metadata = getMetadata(tag);
        if(TagUtil.removeTag(tag.getTag(),metadata)){
            log.info(formatLogMessage("removed", tag));
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("success");
        mav.setViewName("jsonView");
        return mav;
    }
    


}