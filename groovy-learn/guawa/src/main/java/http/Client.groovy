package http

/**
 * User: dingyue
 * Date: 13-3-15
 * Time: 下午9:39
 */

//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0-RC2' )
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

def http = new HTTPBuilder('http://ajax.googleapis.com')

// perform a GET request, expecting JSON response data
http.request(GET, JSON) {
    uri.path = '/ajax/services/search/web'
    uri.query = [v: '1.0', q: 'Calvin and Hobbes']

    headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'

    // response handler for a success response code:
    response.success = { resp, json ->
        println resp.statusLine

        // parse the JSON response object:
        json.responseData.results.each {
            println "  ${it.titleNoFormatting} : ${it.visibleUrl}"
        }
    }

    // handler for any failure status code:
    response.failure = { resp ->
        println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
    }
}
def baiduHttp = new HTTPBuilder("http://www.baidu.com/");
baiduHttp.request(GET, HTML) {
    response.success = { resp, html ->
        println html
    }
}

def subwayHttp = new HTTPBuilder("http://subway.simba.taobao.com/");
subwayHttp.request(GET, HTML) {
    response.success = { resp, html ->
        println html
    }
}


