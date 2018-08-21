package hap.exam.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.exam.dto.OrgCompanys;
import hap.exam.service.IOrgCompanysService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by apex_yang on 2018/8/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgCompanysServiceImpl extends BaseServiceImpl<OrgCompanys> implements IOrgCompanysService {
}
