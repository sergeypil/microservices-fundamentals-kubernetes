package contracts

import org.springframework.cloud.contract.spec.Contract

//[
//        Contract.make {
//            description 'should save audio file'
//            name 'save audio file'
//            request {
//                method 'POST'
//                url '/resources'
//                headers {
//                    contentType('multipart/form-data')
//                }
//                multipart([
//                        [name: 'audioFile', data: RandomStringUtils.randomAlphanumeric(10), filename: 'audioFile.mp3', contentType: 'audio/mpeg']
//                ])
//            }
//            response {
//                status 200
//                headers {
//                    contentType('application/json')
//                }
//                body (['id': $(regex('\\d+'))])
//            }
//        },

//        Contract.make {
//            description 'should get Audio by Filename'
//            name 'get audio by filename'
//            request {
//                method 'GET'
//                urlPath '/resources/filename/filename'
//            }
//            response {
//                status 200
//            }
//        },
//
//        Contract.make {
//            description 'should get Audio Url by Id'
//            name 'get audio url by id'
//            request {
//                method 'GET'
//                urlPath '/resources/location/1'
//            }
//            response {
//                status 200
//                body('"http://localhost:8585/resources/1"')
//            }
//        },
//
//        Contract.make {
//            description 'should get Audio resource by Id'
//            name 'get audio resource by id'
//            request {
//                method 'GET'
//                urlPath '/resources/1'
//            }
//            response {
//                status 200
//                headers {
//                    contentType('application/json')
//                }
//            }
//        }
//]