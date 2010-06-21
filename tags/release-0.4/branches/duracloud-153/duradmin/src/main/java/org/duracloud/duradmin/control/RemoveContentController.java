
package org.duracloud.duradmin.control;

import org.apache.log4j.Logger;
import org.duracloud.client.ContentStore;
import org.duracloud.duradmin.domain.ContentItem;
import org.duracloud.duradmin.domain.Space;
import org.duracloud.duradmin.util.ControllerUtils;
import org.duracloud.duradmin.util.MessageUtils;
import org.duracloud.duradmin.util.SpaceUtil;
import org.springframework.binding.message.Message;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveContentController
        extends BaseCommandController {

    protected final Logger log = Logger.getLogger(getClass());

    public RemoveContentController() {
        setCommandClass(ContentItem.class);
        setCommandName("contentItem");
    }

    @Override
    protected ModelAndView handle(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Object command,
                                  BindException errors) throws Exception {
        ContentItem contentItem = (ContentItem) command;
        String spaceId = contentItem.getSpaceId();
        String contentId = contentItem.getContentId();
        ControllerUtils.checkContentItemId(spaceId, contentId);
        ContentStore store = getContentStore();
        store.deleteContent(spaceId, contentId);
        Space space = new Space();
        space.setSpaceId(spaceId);
        SpaceUtil.populateSpace(space, store.getSpace(spaceId, null, 0, null));
        Message message =
                MessageUtils.createMessage("Successfully removed content item");

        ModelAndView mav = new ModelAndView();
        mav.addObject("space", space);
        return setView(request, mav, message);
    }

}