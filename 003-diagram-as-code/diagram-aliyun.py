# diagram.py
from diagrams import Diagram
from diagrams.alibabacloud.compute  import ECS
from diagrams.alibabacloud.database import RDS
from diagrams.alibabacloud.network import SLB

with Diagram("Web Service aliyun", show=True):
    SLB("lb") >> ECS("web") >> RDS("userdb")
