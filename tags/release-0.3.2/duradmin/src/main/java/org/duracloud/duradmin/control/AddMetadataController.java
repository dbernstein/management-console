
package org.duracloud.duradmin.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.duracloud.duradmin.contentstore.ContentItemList;
import org.duracloud.duradmin.contentstore.ContentItemListCache;
import org.duracloud.duradmin.domain.MetadataItem;
import org.duracloud.duradmin.util.MessageUtils;
import org.duracloud.duradmin.util.MetadataUtils;
import org.springframework.binding.message.Message;
import org.springframework.binding.message.Severity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class AddMetadataController
        extends MetadataController {

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    protected ModelAndView handleMetadataItem(HttpServletRequest request,
                                              HttpServletResponse response,
                                              MetadataItem metadataItem,
                                              BindException errors)
            throws Exception {
        ModelAndView mav = new ModelAndView();
        Message message;
        Map<String, String> metadata = getMetadata(metadataItem);
        boolean isNew =
                MetadataUtils.add(metadataItem.getName(), metadataItem
                        .getValue(), metadata) == null;
        setMetadata(metadata, metadataItem);

        //mark content item list for update if a spaces 
        if (!StringUtils.hasText(metadataItem.getContentId())) {
            ContentItemList contentItemList =
                    ContentItemListCache.get(request,
                                             metadataItem.getSpaceId(),
                                             getContentStoreProvider());
            contentItemList.markForUpdate();
            mav.addObject("contentItemList", contentItemList);
            mav.addObject("space", contentItemList.getSpace());

        }


        
        log.info(formatLogMessage("added", metadataItem));

        if (isNew) {
            message = MessageUtils.createMessage("Successfully added metadata");
        } else {
            message =
                    MessageUtils.createMessage("Metadata value replaced.",
                                               Severity.WARNING);
        }
        return setView(request, mav, message);
    }

}