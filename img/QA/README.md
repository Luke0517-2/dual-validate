# 檔案說明
**資料夾架構比照現行mBMS交付 CI/CD 資料夾結構，以 $ns\$api-resource 做區隔**  
|檔案名稱|用途|備註|  
|---|---|---|  
|istio-morder-qa\qa-gw.yaml|istio-morder-qa gateway 設定檔案||  
|istio-morder-qa\qa-svc.yaml|istio-morder-qa service 設定檔案||  
|morder-qa\qa-deploy-dualvalidateservice.yaml|dual validate service deploy 設定檔案||  
|morder-qa\qa-secret-harbor.yaml|morder-qa 存取cht harbor secret 檔案||  
|morder-qa\qa-svc-dualvalidateservice.yaml|dual validate service svc 設定檔案||  
|morder-qa\qa-vs-dualvalidateservice.yaml|morder-qa istio 對應的 virtual service 設定檔||  

*YAML命名規則：${環境識別代碼}-${[對應 K8s API資源縮寫](https://kubernetes.io/docs/reference/kubectl/#operations)}-${模組名稱|系統功能}

# 操作說明
** 透過 Kubectl 指令進行部署操作 **  
1. 在已經安裝 gcloud cli 的主機上執行：gcloud auth login  
2. 進行部署設定，在對應目錄下執行：kubectl apply -f ./  
* 範例： kubectl apply -f ./istio-morder-qa/ -n istio-morder-qa && kubectl apply -f ./morder-qa/ -n morder-qa  
3. 移除部署設定，在對應目錄下執行：kubectl delete -f ./  
* 範例： kubectl delete -f ./istio-morder-qa/ -n istio-morder-qa && kubectl delete -f ./morder-qa/ -n morder-qa


# 檢驗說明
** 若有取得回傳，代表已成功部署且連到對應地端資料庫 **  
1. OA 至 ingress ip (10.183.2.53) 未開通前，可在RDP跳板上透過瀏覽器呼叫
http://10.183.2.53/ORMemoryCenter/jsp/queryMemoryCenter.jsp?tablename=mordersysparameter&keyfield=code  

2. [chtdeip 防火牆開通](https://chtdeip.cht.com.tw/Portal?xpc=1$@60$@1$@50)、eform [IS-10](http://eform.cht.com.tw/UserForms/IS10/Apply.aspx) & [IS-14](http://eform.cht.com.tw/UserForms/IS14/Apply.aspx) 皆完成後，需在 qa-gw.yaml 完成憑證設定，就可在特定 OA IP 區間，透過瀏覽器呼叫  
https://mbms-order-qa.cht.com.tw/ORMemoryCenter/jsp/queryMemoryCenter.jsp?tablename=applyrule&keyfield=productid


# 參考文件
[gcloud cli](https://cloud.google.com/sdk/gcloud)  
[行動維運處寫的指引的Google Cloud Anthos on Baremetal](https://cht365.sharepoint.com/sites/F20/Shared%20Documents/Forms/AllItems.aspx?FolderCTID=0x012000DC19CEFE1CB3E54EADF3D1A2AEEABB4E&id=%2Fsites%2FF20%2FShared%20Documents%2FCCBOSS%2F1%2E%E8%BB%9F%E9%AB%94%E6%9E%B6%E6%A7%8B%E8%88%87%E7%99%BC%E5%B1%95%E7%B5%84%2FPaaS%E7%94%B3%E8%AB%8B%E8%A1%A8%E5%96%AE%2F2%2E4%5F20220705%5FGoogle%20Cloud%20Anthos%20On%20Baremetal%2Epdf&parent=%2Fsites%2FF20%2FShared%20Documents%2FCCBOSS%2F1%2E%E8%BB%9F%E9%AB%94%E6%9E%B6%E6%A7%8B%E8%88%87%E7%99%BC%E5%B1%95%E7%B5%84%2FPaaS%E7%94%B3%E8%AB%8B%E8%A1%A8%E5%96%AE)