package contracts

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            description 'Should save the song metadata successfully'
            request {
                method 'POST'
                url '/songs'
                body("""
            {
                "name": "Test Song",
                "artist": "Test Artist",
                "album": "Test Album",
                "length": "3:30",
                "resourceId": 1,
                "year": "2000",
                "genre": "Test Genre"
            }
        """)
                headers {
                    header 'Content-Type': 'application/json'
                }
            }
            response {
                status 200
                body("""
            {
                "id": 1
            }
        """)
                headers {
                    header 'Content-Type': 'application/json'
                }
            }
        },

        Contract.make {
            description 'Should fetch the song metadata successfully'
            request {
                method 'GET'
                url '/songs/1'
            }
            response {
                status 200
                body("""
            {
                "name": "Test Song",
                "artist": "Test Artist",
                "album": "Test Album",
                "length": "3:30",
                "resourceId": 1,
                "year": "2000",
                "genre": "Test Genre"
            }
        """)
                headers {
                    header 'Content-Type': 'application/json'
                }
            }
        },

        Contract.make {
            description 'Should delete the song metadata successfully'
            request {
                method 'DELETE'
                url '/songs?id=1,2,3'
            }
            response {
                status 200
                body("""
            [1,2,3]
        """)
                headers {
                    header 'Content-Type': 'application/json'
                }
            }
        }
]