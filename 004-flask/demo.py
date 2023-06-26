from flask import Flask,request,render_template
app=Flask(__name__)

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



app.run(host="127.0.0.1",port=5005,debug=True)
