package contracts

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            description 'should save resource'
            name 'should save resource'
            request {
                method POST()
                url '/resources'
                headers {
                    contentType multipartFormData()
                }
                multipart(
                        audioFile: named(
                                name: $("sample.mp3"),
                                content: $("test content")
                        )
                )
            }
            response {
                status 200
                headers {
                    contentType applicationJson()
                }
                body """
            {
                "id": 123
            }
            """
            }
        },

        Contract.make {
            description 'should get resource url by id'
            name 'should get resource url by id'
            request {
                method 'GET'
                urlPath '/resources/location/1'
            }
            response {
                status 200
                body('"http://localhost:8585/resources/1"')
            }
        },

        Contract.make {
            description 'should get resource by id'
            name 'should get resource by id'
            request {
                method 'GET'
                urlPath '/resources/1'
            }
            response {
                status 200
                headers {
                    contentType('application/json')
                }
            }
        },
        Contract.make {
            description "should delete audio resources by their ids and return them"
            request {
                method DELETE()
                url(value('/resources?id=1,2,3'))
            }
            response {
                status 200
            }
        },
        Contract.make {
            description("should move audio by its id")
            request {
                method PUT()
                urlPath('/resources/1') 
            }
            response {
                status OK() 
            }
        }
]