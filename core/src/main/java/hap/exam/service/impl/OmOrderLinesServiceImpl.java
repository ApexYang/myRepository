package hap.exam.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.exam.dto.InvInventoryItems;
import hap.exam.service.IInvInventoryItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hap.exam.dto.OmOrderLines;
import hap.exam.service.IOmOrderLinesService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmOrderLinesServiceImpl extends BaseServiceImpl<OmOrderLines> implements IOmOrderLinesService{

    @Autowired
    private IOmOrderLinesService omOrderLinesService;
    @Autowired
    private IInvInventoryItemsService invInventoryItemsService;

    @Override
    public List<OmOrderLines> mySelect(IRequest requestContext, OmOrderLines dto, int page, int pageSize) {
        List<OmOrderLines> select = omOrderLinesService.select(requestContext, dto, page, pageSize);
        if(!select.isEmpty()&&select.size()>0){
            for (int i = 0; i <select.size() ; i++) {
                OmOrderLines omOrderLines = select.get(i);
                //物料描述
                InvInventoryItems invInventoryItems = new InvInventoryItems();
                invInventoryItems.setInventoryItemId(omOrderLines.getInventoryItemId());
                InvInventoryItems invInventoryItems1 = invInventoryItemsService.selectByPrimaryKey(requestContext, invInventoryItems);
                omOrderLines.setItemDescription(invInventoryItems1.getItemDescription());
                omOrderLines.setItemCode(invInventoryItems1.getItemCode());
                //订单金额
                Long temp = omOrderLines.getOrderdQuantity()*omOrderLines.getUnitSellingPrice();
                omOrderLines.setOrderMoney(temp);
            }
        }
        return select;
    }
}