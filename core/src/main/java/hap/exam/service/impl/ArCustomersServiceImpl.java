package hap.exam.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;
import hap.exam.dto.ArCustomers;
import hap.exam.service.IArCustomersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by apex_yang on 2018/8/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ArCustomersServiceImpl extends BaseServiceImpl<ArCustomers> implements IArCustomersService {
}
