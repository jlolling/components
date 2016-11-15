package org.talend.components.service.rest;

import static com.jayway.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.talend.components.service.rest.DefinitionType.COMPONENT;
import static org.talend.components.service.rest.DefinitionType.DATA_STORE;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.talend.components.api.RuntimableDefinition;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.common.datastore.DatastoreDefinition;
import org.talend.components.service.rest.dto.DefinitionDTO;
import org.talend.components.service.rest.mock.MockComponentDefinition;
import org.talend.components.service.rest.mock.MockDatastoreDefinition;
import org.talend.daikon.definition.service.DefinitionRegistryService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * Unit test for the org.talend.components.service.rest.DefinitionsController class.
 *
 * @see DefinitionsController
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class DefinitionsControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private DefinitionRegistryService delegate;

    @Autowired
    private DefinitionsController controller;

    @Autowired
    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
    }

    @Test
    public void shouldListDatastoreDefinitions() throws Exception {
        shouldListDefinitions(Arrays.asList("toto", "tutu", "titi"), //
                DatastoreDefinition.class,  //
                this::getDatastoreDefinitions, //
                DATA_STORE,  //
                "datastore");
    }

    @Test
    public void shouldListComponentDefinitions() throws Exception {
        shouldListDefinitions(Arrays.asList("one", "two", "three"), //
                ComponentDefinition.class,  //
                this::getComponentDefinitions, //
                COMPONENT,  //
                "component");
    }

    public void shouldListDefinitions(List<String> names, //
                                      Class clazz, //
                                      Function<List<String>, Map<String, ? extends RuntimableDefinition>> provider, //
                                      DefinitionType wantedType,  //
                                      String expectedType) throws IOException {
        // given
        BDDMockito.given(delegate.getDefinitionsMapByType(clazz)) //
                .willReturn(provider.apply(names));

        // when
        final Response response = when().get("/definitions/" + wantedType).andReturn();

        // then
        assertEquals(OK.value(), response.getStatusCode());
        List<DefinitionDTO> actual = objectMapper.readValue(response.asInputStream(), new TypeReference<List<DefinitionDTO>>(){});
        assertEquals(names.size(), actual.size());
        actual.forEach(d -> {
            assertEquals(expectedType, d.getType()); // it's a component
            assertTrue(names.contains(d.getName().substring("mock ".length()))); // it's expected
        });
    }

    private Map<String, DatastoreDefinition> getDatastoreDefinitions(List<String> names) {
        Map<String, DatastoreDefinition> definitions = new HashMap<>();
        for (String name : names) {
            definitions.put(name, new MockDatastoreDefinition(name));
        }
        return definitions;
    }

    private Map<String, ComponentDefinition> getComponentDefinitions(List<String> names) {
        Map<String, ComponentDefinition> definitions = new HashMap<>();
        for (String name : names) {
            definitions.put(name, new MockComponentDefinition(name));
        }
        return definitions;
    }

    @Test
    public void shouldFilterComponentsByTopology() throws Exception {
        // given

        // when

        // then

    }
}