package platform;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
public class CodeController {
    private final CodeBook myCodeBook = new CodeBook();

    @GetMapping(value = "/code/{uuid}", produces = "text/html")
    public String code(@PathVariable String uuid) {
        Code myCode = myCodeBook.getCode(uuid);
        if (myCode.viewrestric() && myCode.getViews() < 0 ||
                myCode.timerestric() && myCode.getTime() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "<html>\n" +
                "<link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>" +
                "<head>\n" +
                "    <title>Code</title>\n" +
                "<span id=\"load_date\">" + myCode.getDate().toString() + "</span>" +
                "</head>\n" +
                "<body>\n" +
                (myCode.timerestric() ? String.format("<span id=\"time_restriction\"> %s </span>", myCode.getTime()) : "" ) +
                (myCode.viewrestric() ? String.format("<span id=\"views_restriction\"> %s </span>", myCode.getViews()) : "" ) +
                "<pre id=\"code_snippet\"><code>\n" +
                myCode.getCode() +
                "</code></pre>\n" +
                "</body>\n" +
                "</html>";
    }



    @GetMapping(value = "/api/code/{uuid}")
    public ResponseEntity<Code> getcodebyid(@PathVariable String uuid) {
        Code myCode = myCodeBook.getCode(uuid);
        if (myCode.viewrestric() && myCode.getViews() < 0 ||
        myCode.timerestric() && myCode.getTime() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(myCode);
    }

    @PostMapping(value = "/api/code/new", produces = "application/json")
    public ResponseEntity<Map<String,String>> newCode(@RequestBody simpleCode newcode) {
        System.out.println("Code:" + newcode.getCode());
        return ResponseEntity.ok(Map.of("id",
                myCodeBook.addCode(newcode.getCode(), newcode.getTime(), newcode.getViews())));
    }

    @GetMapping("/code/new")
    public String newCodebyHtml() {
        return "<html>\n" +
                "<link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>" +
                "<head>\n" +
                "<title>Create</title>\n" +
                "<span id=\"load_date\">" + LocalDate.now() + "</span>" +
                "</head>\n" +
                "<body>\n" +
                "<textarea id=\"code_snippet\"> ... </textarea>" +
                "<div>Time restrictions<input id=\"time_restriction\" type=\"text\"/></div>" +
                "<div>View restrictions<input id=\"views_restriction\" type=\"text\"/></div>" +
                "<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>" +
                "<script>" +
                "function send() {\n" +
                "    let object = {\n" +
                "        \"code\": document.getElementById(\"code_snippet\").value\n" +
                "    };\n" +
                "    \n" +
                "    let json = JSON.stringify(object);\n" +
                "    \n" +
                "    let xhr = new XMLHttpRequest();\n" +
                "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "    xhr.send(json);\n" +
                "    \n" +
                "    if (xhr.status == 200) {\n" +
                "      alert(\"Success!\");\n" +
                "    }\n" +
                "}" +
                "</script>" +
                "</pre>\n" +
                "</body>\n" +
                "</html>";
    }

    @GetMapping(value = "/api/code/latest", produces = "application/json")
    public List<Code> getlatest() {
        return myCodeBook.getLatestTen();
    }

    @GetMapping("/code/latest")
    public String getlatesthtml(Model model) {
        List<Code> firstten = myCodeBook.getLatestTen();
        StringBuilder mylist = new StringBuilder();
        for (Code code : firstten) {
            mylist.append("<span>")
                    .append(code.getDate().toString())
                    .append("</span>\n").append("<pre><code>")
                    .append(code.getCode())
                    .append("</code></pre>\n");
        }

        return "<html lang=\"en\">\n" +
                "<link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>" +
                "<head>\n" +
                "    <title>Latest</title>\n" +
                "</head>\n" +
                "<body>\n" +
                mylist +
                "</body>\n" +
                "</html>";

    }
}
