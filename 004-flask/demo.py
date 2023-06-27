from flask import Flask,request,render_template,session
import json
app=Flask(__name__)

@app.route('/',methods=['GET'])
def index():
    return render_template('index.html')

@app.route('/login', methods = ['GET','POST'])
def login():
    if request.method == 'POST':
        if request.form['username'] == request.form['password']:
            return 'TRUE'
        else:
            #当form中的两个字段内容不一致时，返回我们所需要的测试信息
            return str(request.headers)        #需要替换的部分
    else:
        return render_template('login.html',h2content="this is h2 header content")

@app.before_request
def before():
    url = request.path  # 当前请求的URL
    passUrl = ["/login", "/regist","/json"]
    if url in passUrl:
        print( "passUrl is %s" , url)
        pass
    else:
        _id = session.get("_id", None)
        if not _id:
            return result(203, {"info": "未登录"})
        else:
            pass



@app.route('/upload',methods=['GET','POST'])
def upload():
    if request.method == 'POST':
        f = request.files['file']
        filename = secure_filename(f.filename)
        #f.save(os.path.join('app/static',filename))
        f.save('app/static/'+str(filename))
        return 'ok'
    else:
        return render_template('upload.html')

@app.route('/json',methods=['GET','POST'])
def json_method():
    data = {
        "name":"zhangsan",
        "age":18
    }
    # 第一种
    response = json.dumps(data)  # 将python的字典转换为json字符串
    return response,200,{"Content-Type":"application/json"}


app.run(host="127.0.0.1",port=5005,debug=True)
