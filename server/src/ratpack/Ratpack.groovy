import me.libs.server.api.handler.BookHandler
import me.libs.server.api.handler.HandlerModule
import me.libs.server.api.handler.LibraryHandler
import me.libs.server.api.handler.LibraryOperationsHandler
import me.libs.server.api.handler.LogInHandler
import me.libs.server.api.handler.SignUpHandler
import me.libs.server.persistence.PersistenceService
import me.libs.server.persistence.PersistenceServiceModule
import me.libs.server.security.ApiBasicAuthentication
import me.libs.server.security.SecurityService
import me.libs.server.security.SecurityServiceModule
import me.libs.server.security.Subject
import ratpack.jackson.JacksonModule
import ratpack.registry.Registries

import static ratpack.groovy.Groovy.ratpack

ratpack {

    bindings {
        module JacksonModule
        module PersistenceServiceModule
        module SecurityServiceModule
        module HandlerModule
    }

    handlers {
        get('hello') { PersistenceService persistenceService ->
            render persistenceService.sayHello()
        }
        prefix('api/v1') {
            prefix('auth') {
                handler('signup', SignUpHandler)
                handler('login', LogInHandler)
            }
            handler { SecurityService securityService ->
                def subject = new ApiBasicAuthentication(securityService).resolve(request.headers)
                if (subject != Subject.ANYONYMOUS) {
                    next(Registries.just(Subject, subject))
                } else {
                    response.status(401).send()
                }
                next()
            }
            prefix('library') {
                handler(':user/:library?', LibraryHandler);
                handler(':user/:library/book/:id?', LibraryOperationsHandler);
            }
            handler('book/:id?', new BookHandler())
        }
    }
}