this is a demo for https://diagrams.mingrammer.com/

preinstall

- Graphviz
    - check
    - install guide `brew install Graphviz`


```bash
pip install diagrams
```

```python
# diagram.py
from diagrams import Diagram
from diagrams.aws.compute import EC2
from diagrams.aws.database import RDS
from diagrams.aws.network import ELB

with Diagram("Web Service", show=False):
    ELB("lb") >> EC2("web") >> RDS("userdb")
```

```bash
python diagram.py
```
