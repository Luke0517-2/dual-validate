# 檔案說明
**資料夾架構比照現行mBMS交付 CI/CD 資料夾結構，以 $ns\$api-resource 做區隔**  
|檔案名稱|用途|備註|  
|---|---|---|  
|mbms-morder-feature\qa-deploy-dualvalidateservice.yaml|dual validate service deploy 設定檔案||  
|mbms-morder-feature\iisi-gw-dualvalidateservice.yaml|istio-morder-qa gateway 設定檔案||  
|mbms-morder-feature\qa-deploy-dualvalidateservice.yaml|dual validate service deploy 設定檔案||  
mbms-morder-feature\qa-svc-dualvalidateservice.yaml|dual validate service svc 設定檔案||  
|mbms-morder-feature\qa-vs-dualvalidateservice.yaml|morder-qa istio 對應的 virtual service 設定檔||  

*YAML命名規則：${環境識別代碼}-${[對應 K8s API資源縮寫](https://kubernetes.io/docs/reference/kubectl/#operations)}-${模組名稱|系統功能}

# 操作說明
** 透過 Kubectl 指令進行部署操作 **  
1. 在已經安裝 oc cli 的主機上執行：oc login  
2. 進行部署設定，在對應目錄下執行：kubectl apply -f ./  
* 範例： kubectl apply -f ./mbms-morder-feature/ -n mbms-morder-feature 
3. 移除部署設定，在對應目錄下執行：kubectl delete -f ./  
* 範例： kubectl delete -f ./mbms-morder-feature/ -n mbms-morder-feature 


# 檢驗說明
** 若有取得回傳，代表已成功部署且連到對應PVC**  
1. 可透過瀏覽器呼叫
http://dualvalidateservice-istio-system.apps.ocp.iisi.test/cht/validate/startTest
回傳uuid即為正常


# 參考文件
