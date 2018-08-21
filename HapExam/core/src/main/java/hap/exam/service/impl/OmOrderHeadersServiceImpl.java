package hap.exam.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.exam.dto.ArCustomers;
import hap.exam.dto.OmOrderHeaders;
import hap.exam.dto.OmOrderLines;
import hap.exam.dto.OrgCompanys;
import hap.exam.service.IArCustomersService;
import hap.exam.service.IOmOrderHeadersService;
import hap.exam.service.IOmOrderLinesService;
import hap.exam.service.IOrgCompanysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmOrderHeadersServiceImpl extends BaseServiceImpl<OmOrderHeaders> implements IOmOrderHeadersService{

    @Autowired
    private IOmOrderHeadersService omOrderHeadersService;

    @Autowired
    private IOmOrderLinesService omOrderLinesService;

    @Autowired
    private IOrgCompanysService orgCompanysService;

    @Autowired
    private IArCustomersService arCustomersService;

    //批量插入或修改
    @Override
    public List<OmOrderHeaders> myBatchUpdate(IRequest requestCtx, List<OmOrderHeaders> dto) {
        //判断是否为空
        if(!dto.isEmpty()&&dto.size()>0){
            for(int i=0;i<dto.size();i++){
                OmOrderHeaders omOrderHeaders = dto.get(i);
                //判断是修改还是插入
                if(omOrderHeaders.getHeaderId()==null||omOrderHeaders.getHeaderId()==0){
                    //insert
                    //插入头
                    omOrderHeadersService.insertSelective(requestCtx,omOrderHeaders);
                    //插入行
                    List<OmOrderLines> omOrderLines = omOrderHeaders.getOrderLinesList();
                    if(!omOrderLines.isEmpty()&&omOrderLines.size()>0){
                        for(int j=0;j<omOrderLines.size();j++){
                            OmOrderLines omOrderLine = omOrderLines.get(j);
                            omOrderLine.setCompanyId(omOrderHeaders.getCompanyId());
                            omOrderLine.setHeaderId(omOrderHeaders.getHeaderId());
                            omOrderLinesService.insertSelective(requestCtx,omOrderLine);
                        }
                    }

                }else{
                    //update
                    omOrderHeadersService.updateByPrimaryKeySelective(requestCtx,omOrderHeaders);
                    List<OmOrderLines> omOrderLines = omOrderHeaders.getOrderLinesList();
                    if(omOrderLines!=null&&!omOrderLines.isEmpty()&&omOrderLines.size()>0){
                        for(int j=0;j<omOrderLines.size();j++){
                            OmOrderLines lines = omOrderLines.get(j);
                            if(lines.getLineId()!=null&&lines.getLineId()!=0){
                                omOrderLinesService.updateByPrimaryKeySelective(requestCtx,lines);
                            }else {
                                lines.setCompanyId(omOrderHeaders.getCompanyId());
                                lines.setHeaderId(omOrderHeaders.getHeaderId());
                                omOrderLinesService.insertSelective(requestCtx,lines);
                            }
                        }
                    }

                }
            }
        }
        return dto;
    }

    @Override
    public List<OmOrderHeaders> mySelect(IRequest requestContext, OmOrderHeaders dto, int page, int pageSize) {
        List<OmOrderHeaders> select = omOrderHeadersService.select(requestContext, dto, page, pageSize);
        if(!select.isEmpty()&&select.size()>0){
            for (int i = 0; i < select.size(); i++) {
                OmOrderHeaders omOrderHeaders = select.get(i);
                //公司名称
                OrgCompanys orgCompanys = new OrgCompanys();
                orgCompanys.setCompanyId(omOrderHeaders.getCompanyId());
                OrgCompanys orgCompanys1 = orgCompanysService.selectByPrimaryKey(requestContext, orgCompanys);
                omOrderHeaders.setCompanyName(orgCompanys1.getCompanyName());
                //客户名称
                ArCustomers arCustomers = new ArCustomers();
                arCustomers.setCustomerId(omOrderHeaders.getCustomerId());
                ArCustomers arCustomers1 = arCustomersService.selectByPrimaryKey(requestContext, arCustomers);
                omOrderHeaders.setCustomerName(arCustomers1.getCustomerName());
                //总金额
                OmOrderLines omOrderLines = new OmOrderLines();
                omOrderLines.setHeaderId(omOrderHeaders.getHeaderId());
                List<OmOrderLines> select1 = omOrderLinesService.select(requestContext, omOrderLines, 1, 0);
                Long count=0l;
                if(!select1.isEmpty()&&select1.size()>0){
                    for (int j = 0; j <select1.size() ; j++) {
                        OmOrderLines omOrderLines1 = select1.get(j);
                        Long temp = omOrderLines1.getOrderdQuantity()*omOrderLines1.getUnitSellingPrice();
                        count+=temp;
                    }
                }
                omOrderHeaders.setOrderMoney(count);
            }
        }

        return select;
    }


    @Override
    public int myBatchDelete(IRequest requestContext, Long headerId) throws Exception {
        int count = 0;

            OmOrderHeaders omOrderHeaders = new OmOrderHeaders();
            omOrderHeaders.setHeaderId(headerId);
            //查询头id对应的行
            OmOrderLines omOrderLines = new OmOrderLines();
            omOrderLines.setHeaderId(headerId);
            List<OmOrderLines> omOrderLinesList = omOrderLinesService.select(requestContext, omOrderLines, 1, 0);
            if(omOrderLinesList != null || ! omOrderLinesList.isEmpty()){
                //删除行
                omOrderLinesService.batchDelete(omOrderLinesList);
            }
            int n = omOrderHeadersService.deleteByPrimaryKey(omOrderHeaders);
            count += n;


        return 0;
    }
}