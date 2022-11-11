# 查核驗證 RESTful API server

## Overview  

1. Swagger UI - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
2. Swagger API Doc - [http://localhost:8080/v3/api-docs/](http://localhost:8080/v3/api-docs/)
3. Spring Actuator - [http://localhost:8080/actuator](http://localhost:8080/actuator)

## Runtime

1. pass any variables with -D options, ex: 
		
		-Dcompare.output.path=D:\workspace\pdc-validate\etc\CHT\  --change output folder
## Coverage Report

Running the below command:

```
mvn test  jacoco:report
```

You can see the code-coverage report , in the folder "target/site/jacoco/index.html"

## Site Report

Running the below command:

```
mvn site
```
You can see the code-coverage report , in the folder "target/site/index.html"

It will generate the Javadoc for public members (defined in <reporting/>) using the given stylesheet (defined in <reporting/>), and with an help page (default value for nohelp is true).

## Java Documentation
Running the below command:

```
mvn javadoc:javadoc
```
It will generate the Javadoc for private members (defined in <build/>) using the stylesheet (defined in <reporting/>), and with no help page (defined in <build/>).

## Minikube Test

```bash
$ minikube start
$ minikube dockerenv
$ eval $(minikube -p minikube docker-env)
$ docker build -t pdc-validate:latest .
```

Maybe you can use the docker registry.

```bash
$ (winpty) docker login https://chtpdc-validate.azurecr.io -u chtpdc-validate
$ docker pull chtpdc-validate.azurecr.io/pdc-validate
```

We can use docker to test basic usage.

```bash
 docker  run  -p 8080:8080  --name test  pdc-validate:latest
```

If it's ok , we test minikube .

```bash
$ kubectl  apply -f k8s-yml/pdc-validate-volume.yaml
$ kubectl get pvc
NAME              STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS   AGE
pdc-validate-compares   Bound    pvc-8c570920-a84b-495e-83a4-b1485709eb8c   2Gi        RWO            standard       9s

$ kubectl apply -f k8s-yml/pdc-validate-deployment.yaml
deployment.apps/pdc-validate created

$ kubectl apply -f k8s-yml/pdc-validate-service.yaml
service/pdc-validate created

$ kubectl get services
NAME         TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)    AGE
kubernetes   ClusterIP   10.96.0.1      <none>        443/TCP    22m
pdc-validate       ClusterIP   10.110.55.72   <none>        8080/TCP   7s

$ kubectl apply -f k8s-yml/pdc-validate-route.yaml
error: unable to recognize "pdc-validate-route.yaml": no matches for kind "Route" in version "route.openshift.io/v1"
```

Because  kind "Route" is the OCP's resource .


## OCP Sandbox Test

### Tutorials
[examples](https://developers.redhat.com/developer-sandbox/activities)

#### Homework 01: Learn Kubernetes using the Developer Sandbox for Red Hat OpenShift
https://developers.redhat.com/developer-sandbox/activities/learn-kubernetes-using-red-hat-developer-sandbox-openshift


#### Evironment preparation

```bash

wget https://mirror.openshift.com/pub/openshift-v4/clients/oc/latest/linux/oc.tar.gz
sudo tar -xvzf oc.tar.gz  
sudo chmod +x  oc  kubectl 
sudo mv oc /usr/local/bin/oc
sudo mv kubectl /usr/local/bin/kubectl
sudo usermod -aG docker $USER

oc login --token=sha256~Ma2K1jVjyKqIdV2OMHDk36ysKWXEaC0wCqFHJqDfoiA --server=https://api.sandbox.x8i5.p1.openshiftapps.com:6443

export cluster_name=api-sandbox-x8i5-p1-openshiftapps-com:6443
export token=sha256~Ma2K1jVjyKqIdV2OMHDk36ysKWXEaC0wCqFHJqDfoiA
export username=robert0714-lee
export api_server_url=https://api.sandbox.x8i5.p1.openshiftapps.com:6443

kubectl config set-credentials $username/$cluster_name --token=$token
kubectl config set-cluster $cluster_name --server=$api_server_url

export context=$username-dev/$cluster_name/$username

kubectl config set-context $context --user=$username/$cluster_name   --namespace=$username-dev --cluster=$cluster_name
```

You need to modify  ***image: pdc-validate:latest***   in k8s-yml/pdc-validate-deployment.yaml And then you can test in OCP.
OpenShift Sandbox environment includes two projects (namespaces) and a resource quota of 7 GB RAM, 15GB storage ,1750m  CPU . 

```bash
$ kubectl  apply -f k8s-yml/pdc-validate-volume.yaml
$ kubectl get pvc
NAME              STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS   AGE
pdc-validate-compares   Bound    pvc-8c570920-a84b-495e-83a4-b1485709eb8c   2Gi        RWO            standard       9s

$ kubectl apply -f k8s-yml/pdc-validate-deployment.yaml
deployment.apps/pdc-validate created

$ kubectl apply -f k8s-yml/pdc-validate-service.yaml
service/pdc-validate created

$ kubectl get services
NAME         TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)    AGE
kubernetes   ClusterIP   10.96.0.1      <none>        443/TCP    22m
pdc-validate       ClusterIP   10.110.55.72   <none>        8080/TCP   7s

$ kubectl apply -f k8s-yml/pdc-validate-route.yaml

```
In OCP , you can use the env "JAVA_OPTS" :

```bash
 -XX:+UseParallelGC -XX:MinHeapFreeRatio=5 -XX:MaxHeapFreeRatio=10 -XX:GCTimeRatio=4
```


## Version Naming
```bash
 mvn  --settings settings.xml  --global-settings C:/Users/1204003/.m2/settings.xml clean package -Dmaven.test.skip=true -Dtmf-version=20210730.11
```

## Sonar scan by maven
[refernce](https://github.com/SonarSource/sonar-scanning-examples/blob/master/doc/jacoco.md)

```bash
mvn --settings settings.xml sonar:sonar
```

#### Troubleshooting
To investigate issues with the import of coverage information you can run Maven with the debug flag, ***-X*** :

```bash
mvn  --settings settings.xml  --global-settings C:/Users/1204003/.m2/settings.xml  -X clean verify sonar:sonar 
```

# 中華環境雙軌驗證專案設定

中華環境Azure的repo名稱:
```bash
mbms_dual_validate
```

repo檔案結構:
>待規劃...。目前現況有截圖。

作為input的csv檔名格式
```bash
wildflyVerifyUnLoadDateExe_yyyymmdd.csv
```

更新頻率：一天一次

檔案內容，沒有title row：
```
telnum1,custId1,
telnum2,custId2,
```