# Vaadinator Addressbook8 Example

This example uses the vaadin8 desktop templates.
## Running
This example has different profiles which have to be selected:
- desktop
- tomcat (H2), tomcatex (Postgres) or jboss (Postgres)

To run the tomcat desktop variant call `mvn jetty:run -Pdesktop,tomcat,stageres`, then go with your webbrowser to http://localhost:8080. (stageres is required to include all Vaadin Files into Jetty)
