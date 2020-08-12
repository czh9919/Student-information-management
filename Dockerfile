FROM maven
MAINTAINER dbd039<czh9919@outlook.com>

ENV CODE /code
ENV WORK /code/work
RUN mkdir -p $CODE \
    && mkdir -p $WORK

WORKDIR $WORK

RUN mkdir codeT
COPY ./springboot-demo codeT
RUN cd codeT && mvn compile clean verify
# 这里将项目中./target/*.jar 复制到了 镜像里并命名为app.jar,  为什么是 ./target/*.jar , 因为 maven 打包后的文件就是在该路径, 如果是其他项目,填写对应路径 和名称就行了  
EXPOSE 9033
# 这是运行jar的命令,  如果是其他项目, 填写对应命令就行了
CMD cd codeT && mvn spring-boot:run
