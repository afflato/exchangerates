# HEXAGONAL Arch
# Consume API beeceptor.com
# JACOCO TEST REPORT
# Harness CLOUD
# SONAR CLOUD
# DockerHub

# ArgoCD
https://argo-cd.readthedocs.io/en/stable/
kubectl create namespace argocd
kubectl apply -n argocd --server-side --force-conflicts -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl get all -n argocd;
kubectl get svc -n argocd
(Look for
argocd-server  ClusterIP   10.103.151.115   <none>    80/TCP,443/TCP   131m
)
argocd-server                             ClusterIP   10.103.151.115   <none>        80/TCP,443/TCP               131m
kubectl port-forward svc/argocd-server -n argocd 8091:443
kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath="{.data.password}" | base64 --decode


# Kubernets Manifests Configuration in Cluster
 using helm => https://helm.sh/docs/intro/install/
 curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-4 | bash
1. helm chart
    helm create k8chart

2. create cd
            - application.yaml
            - dockerhub-token and github-token => DO NOT COMMIT THESE 2 files
3. kubectl create namespace app
4. cd devops/cd
5. kubectl apply -f github-token.yaml
6. kubectl apply -f dockerhub-token.yaml
7. kubectl apply -f application.yaml
8. 


