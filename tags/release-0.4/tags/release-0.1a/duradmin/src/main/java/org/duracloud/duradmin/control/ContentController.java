
package org.duracloud.duradmin.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.duracloud.client.ContentStore;
import org.duracloud.duradmin.domain.ContentItem;
import org.duracloud.duradmin.domain.ContentMetadata;
import org.duracloud.duradmin.util.ControllerUtils;
import org.duracloud.duradmin.util.MessageUtils;
import org.duracloud.duradmin.util.SpaceUtil;
import org.duracloud.duradmin.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ContentController
        extends BaseFormController {

    private static final String CONTENT_ITEM = "contentItem";
    protected final Log log = LogFactory.getLog(getClass());

    public ContentController() {
        setCommandClass(ContentItem.class);
        setCommandName(CONTENT_ITEM);
    }

    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return true;
    }
    
    
    @Override
    protected ModelAndView onSubmit(Object command, BindException errors)
            throws Exception {
        ContentItem contentItem = (ContentItem) command;
        String spaceId = contentItem.getSpaceId();
        String contentId = contentItem.getContentId();
        ModelAndView mav = new ModelAndView(getSuccessView());

        ControllerUtils.checkContentRequestParams(spaceId,contentId);


        ContentStore store = null;
        try {
            store = getContentStore();
        } catch (Exception se) {
            mav.setViewName("error");
            mav.addObject("error", se.getMessage());
            return mav;
        }

        Map<String, String> contentMetadata = store.getContentMetadata(spaceId, contentId);

        String action = contentItem.getAction();
        if (action != null && action.equals("update")) {
            String newMime = contentItem.getContentMimetype();
            if (!StringUtils.isEmptyOrAllWhiteSpace(newMime) && 
                    !newMime.equals(contentMetadata.get(ContentStore.CONTENT_MIMETYPE))) {
                Map<String,String> updatedMetadata = new HashMap<String, String>();
                updatedMetadata.put(ContentStore.CONTENT_MIMETYPE, newMime);
                store.setContentMetadata(spaceId, contentId, updatedMetadata);
                MessageUtils.addFlashMessage("Successfully modified content.", mav);
            }
        }

        
        contentMetadata = store.getContentMetadata(spaceId, contentId);
        ContentMetadata metadata = SpaceUtil.populateContentMetadata(contentMetadata);
        contentItem.setMetadata(metadata);
        mav.addObject(CONTENT_ITEM, contentItem);
        mav.addObject("baseURL", store.getBaseURL());
        mav.addObject("storeID", store.getStoreId());
        mav.addObject("title", "Content Detail");
        

        return mav;
    }



}