package com.autumn.gateway;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-01-19  11:14
 **/
public class MainTest {

    public static void main(String[] args){


        String str="{\"W66\": \"http://10.6.197.229:7001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"W67\": \"http://10.6.198.205:7002/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"X01\": \"http://10.6.198.167:8001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"W92\": \"http://10.6.198.205:7002/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"A13\": \"http://10.6.198.167:8003/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"F12\": \"http://10.6.193.13:7001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"V34\": \"http://10.127.184.67:8001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"F12\": \"http://10.6.193.13:7001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"FAW-DZ\": \"http://10.33.51.221:7002/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"V93\": \"http://10.6.199.60:8001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"V92Z03\": \"http://10.6.197.47:7001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"F01\": \"http://10.135.33.14:8004/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"VB5\": \"http://10.6.198.205:7002/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"X11\": \"http://10.6.198.167:8001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"D61\": \"http://10.6.198.167:8001/QMERPServiceAgent/QMERPServiceAgent?wsdl\", \"F01WS\": \"http://10.135.33.182:8005/QMERPServiceAgent/QMERPServiceAgent?wsdl\"}";

        System.out.println("[{\"pluginId\":\"divide\",\"policyId\":\"a0fb8b75123f49d881e0ab0bd500a698\",\"name\":\"负载均衡分配插件\"},{\"pluginId\":\"httpProxy\",\"policyId\":\"a0fb8b75123f49d881e0ab0bd500a698\",\"name\":\"http代理插件\"}]");

        System.out.println("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:agen=\"http://com/faw_qm/erp/framework/integrate/agent\" xmlns:java=\"java:com.faw_qm.erp.framework.integrate.agent\">\n <soapenv:Header>\n <esb:control xmlns:esb=\"http://esb.faw_qm.com\">\n <esb:ERPCompanyCode>W92</esb:ERPCompanyCode>\n </esb:control>\n </soapenv:Header>\n <soapenv:Body>\n <agen:requestWithStringAndReturnString>\n <agen:serviceName>GLDTravelExpQueryService</agen:serviceName>\n <agen:methodName>gldQuery</agen:methodName>\n <agen:param>{\"companyCode\":\"W92\",\"pageSize\":\"20\",\"currentPage\":\"1\",\"fiscalyear\":\"2023\",\"poortraveler\":\"'10001293'\"}</agen:param>\n <agen:config>\n </agen:config>\n </agen:requestWithStringAndReturnString>\n </soapenv:Body>\n</soapenv:Envelope>");

        System.out.println("<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\"><env:Header/><env:Body><m:requestWithStringAndReturnStringResponse xmlns:m=\"http://com/faw_qm/erp/framework/integrate/agent\"><m:return>{\"total\":1,\"pageCount\":1,\"returnMsg\":\"查询成功\",\"pageSize\":20,\"returnStatus\":\"Y\",\"currentPage\":1,\"returnLeads\":[{\"applicationno\":\"2-2023\",\"amountsubsidytotal\":720,\"fundscenter\":\"YSN002\",\"typecode\":\"01\",\"appsettlementnoUrl\":\"http://10.6.225.106/index.html?urlOauth2=true&amp;from=token&amp;userToken=3xWoS9Bg8r7OcS55lHmfldr7g8RS6zGSGA2NwYP8xVnRpg*L0zUdlq&amp;menu_code=GLD358J02&amp;token_param=W92,2023,4-2023&amp;without_header=true&amp;without_header=true&amp;sourceSystemCode=4-2023\",\"dzrq\":null,\"applicationnoUrl\":\"http://10.6.225.106/index.html?urlOauth2=true&amp;from=token&amp;userToken=3xWoS9Bg8r7OcS55lHmfldr7g8RS6zGSGA2NwYP8xVnRpg*L0zUdlq&amp;menu_code=GLD357J02&amp;token_param=W92,2023,2-2023&amp;without_header=true&amp;without_header=true&amp;sourceSystemCode=2-2023\",\"zcf\":579,\"gxzfzt\":null,\"gxzzzt\":null,\"wflstatus\":null,\"docnopz\":null,\"poortravelerdes\":\"张媛\",\"amount\":28329,\"ordno\":null,\"dates\":\"2023-01-17\",\"hwf\":198,\"bxf\":1641,\"postperiodpz\":null,\"pxf\":1921,\"costcenter\":\"N002\",\"costcenterdes\":\"股份机关-财务管理部\",\"fiscalyearpz\":null,\"wflstatusdes\":\"未提交\",\"tpf\":2621,\"zsf\":2765,\"poortraveler\":\"10001293\",\"estimateddistotal\":1697,\"gwmc\":null,\"traveltypedes\":\"一般差旅\",\"grbxje\":7189,\"ordnodes\":null,\"sequence\":1,\"appsettlementno\":\"4-2023\",\"fundscenterdes\":\"股份机关-财务部\",\"ccsjjg\":60,\"enddate\":\"2023-01-06\",\"tripdate\":\"2023-01-01\",\"qtf\":745}]}</m:return></m:requestWithStringAndReturnStringResponse></env:Body></env:Envelope>");
    }
}
