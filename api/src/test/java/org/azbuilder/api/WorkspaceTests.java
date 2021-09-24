package org.azbuilder.api;

import com.yahoo.elide.core.exceptions.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static com.yahoo.elide.test.jsonapi.JsonApiDSL.*;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class WorkspaceTests extends ServerApplicationTests{

    @Test
    @Sql(statements = {
            "DELETE job; DELETE variable; DELETE workspace; DELETE implementation; DELETE version; DELETE module; DELETE FROM provider; DELETE FROM team; DELETE FROM organization;",
            "INSERT INTO organization (id, name, description) VALUES\n" +
                    "\t\t('a42f538b-8c75-4311-8e73-ea2c0f2fb577','Organization','Description');",
            "INSERT INTO team (id, name, manage_workspace, manage_module, manage_provider, organization_id) VALUES\n" +
                    "\t\t('a42f538b-8c75-4311-8e73-ea2c0f2fb579','sample_team', true, true, true, 'a42f538b-8c75-4311-8e73-ea2c0f2fb577');",
            "INSERT INTO workspace (id, name, source, branch, terraform_version, organization_id) VALUES\n" +
                    "\t\t('c05da917-81a3-4da3-9619-20b240cbd7f7','Workspace','https://github.com/AzBuilder/terraform-sample-repository.git', 'main', '0.15.2', 'a42f538b-8c75-4311-8e73-ea2c0f2fb577');"
    })
    void workspaceApiGetTest() {
        when()
                .get("/api/v1/organization/a42f538b-8c75-4311-8e73-ea2c0f2fb577/workspace")
                .then()
                .log().all()
                .body(equalTo(
                        data(
                                resource(
                                        type( "workspace"),
                                        id("c05da917-81a3-4da3-9619-20b240cbd7f7"),
                                        attributes(
                                                attr("branch", "main"),
                                                attr("name", "Workspace"),
                                                attr("source", "https://github.com/AzBuilder/terraform-sample-repository.git"),
                                                attr("terraformVersion", "0.15.2")
                                        ),
                                        relationships(
                                                relation("job"),
                                                relation("organization",true,
                                                        resource(
                                                                type("organization"),
                                                                id("a42f538b-8c75-4311-8e73-ea2c0f2fb577")
                                                        )
                                                ),
                                                relation("variable")
                                        )
                                )
                        ).toJSON())
                )
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Sql(statements = {
            "DELETE job; DELETE variable; DELETE workspace; DELETE implementation; DELETE version; DELETE module; DELETE FROM provider; DELETE FROM team; DELETE FROM organization;",
            "INSERT INTO organization (id, name, description) VALUES\n" +
                    "\t\t('a42f538b-8c75-4311-8e73-ea2c0f2fb577','Organization','Description');",
            "INSERT INTO team (id, name, manage_workspace, manage_module, manage_provider, organization_id) VALUES\n" +
                    "\t\t('a42f538b-8c75-4311-8e73-ea2c0f2fb579','sample_team', true, true, true, 'a42f538b-8c75-4311-8e73-ea2c0f2fb577');",
            "INSERT INTO workspace (id, name, source, branch, terraform_version, organization_id) VALUES\n" +
                    "\t\t('c05da917-81a3-4da3-9619-20b240cbd7f7','Workspace','https://github.com/AzBuilder/terraform-sample-repository.git', 'main', '0.15.2', 'a42f538b-8c75-4311-8e73-ea2c0f2fb577');",
            "INSERT INTO variable (id, variable_key, variable_value, variable_category, sensitive, workspace_id, variable_description, hcl) VALUES\n" +
                    "\t\t('4ea7855d-ab07-4080-934c-3aab429da889','variableKey','variableValue', 'TERRAFORM', false, 'c05da917-81a3-4da3-9619-20b240cbd7f7', 'someDescription', true);"
    })
    void variableApiGetTest() {
        when()
                .get("/api/v1/organization/a42f538b-8c75-4311-8e73-ea2c0f2fb577/workspace/c05da917-81a3-4da3-9619-20b240cbd7f7/variable")
                .then()
                .log().all()
                .body(equalTo(
                        data(
                                resource(
                                        type( "variable"),
                                        id("4ea7855d-ab07-4080-934c-3aab429da889"),
                                        attributes(
                                                attr("category", "TERRAFORM"),
                                                attr("description", "someDescription"),
                                                attr("hcl", true),
                                                attr("key", "variableKey"),
                                                attr("sensitive", false),
                                                attr("value", "variableValue")
                                        ),
                                        relationships(
                                                relation("workspace",true,
                                                        resource(
                                                                type("workspace"),
                                                                id("c05da917-81a3-4da3-9619-20b240cbd7f7")
                                                        )
                                                )
                                        )
                                )
                        ).toJSON())
                )
                .log().all()
                .statusCode(HttpStatus.SC_OK);
    }
}