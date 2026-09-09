# ==============================
# BUILD
# ==============================

FROM eclipse-temurin:8-jdk AS build

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       ant \
       curl \
       unzip \
    && rm -rf /var/lib/apt/lists/*


# GWT 2.7.0

RUN curl -fL \
    https://github.com/gwtproject/gwt/releases/download/2.7.0/gwt-2.7.0.zip \
    -o /tmp/gwt.zip \
    && unzip /tmp/gwt.zip -d /opt \
    && mv /opt/gwt-2.7.0 /opt/gwt \
    && rm /tmp/gwt.zip


# Java dependencies

RUN mkdir -p /opt/libs \
    && curl -fL \
       https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar \
       -o /opt/libs/gson-2.8.9.jar \
    && curl -fL \
       https://repo1.maven.org/maven2/org/mybatis/mybatis/3.5.19/mybatis-3.5.19.jar \
       -o /opt/libs/mybatis-3.5.19.jar \
    && curl -fL \
       https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.13/postgresql-42.7.13.jar \
       -o /opt/libs/postgresql-42.7.13.jar


COPY . .


RUN ant \
    -Dgwt.sdk=/opt/gwt \
    -Dgwt.libs=/opt/libs \
    -Dgwt.java="$(command -v java)" \
    clean war


# ==============================
# RUNTIME
# ==============================

FROM tomcat:9-jdk8-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build \
    /app/phone.war \
    /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]