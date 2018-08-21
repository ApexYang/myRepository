package hap.exam.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hap.exam.dto.OmOrderHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IOmOrderHeadersService extends IBaseService<OmOrderHeaders>, ProxySelf<IOmOrderHeadersService>{

    List<OmOrderHeaders> myBatchUpdate(IRequest requestCtx, List<OmOrderHeaders> dto);

    List<OmOrderHeaders> mySelect(IRequest requestContext, OmOrderHeaders dto, int page, int pageSize);


    int myBatchDelete(IRequest requestContext, Long headerId) throws Exception;
}