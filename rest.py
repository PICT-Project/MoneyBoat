from flask import Flask, jsonify, request
from flask_restful import Resource,Api,reqparse
from passlib.hash import sha256_crypt as crypt
import pymysql

db = pymysql.connect(db='trial', user='root', passwd='', host='localhost', port=3306)

app = Flask(__name__)
api = Api(app)



class Login(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          un = json_data['phone']
          pw = json_data['password']

          cursor = db.cursor()
          sql = "SELECT phone,password FROM login_ where phone= %s"
          param = un
          cursor.execute(sql, param)
          results = cursor.fetchone()

          if results==None:
               return "None"
          if crypt.verify(pw,results[1]):
               return "True"
          else:
               return "False"


class Register(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          name  = json_data['name']
          phone = json_data['phone']
          password = json_data['password']
          password = crypt.encrypt(password)
          cursor = db.cursor()
          try:
               sql1 = "Insert into login_ values(%s,%s)"
               cursor.execute(sql1,(phone,password))
               sql2 = "Insert into Users(phone,name) values(%s,%s)"
               cursor.execute(sql2,(phone,name))
               db.commit()
          except:
               return "False"
          return "True"

          #verify and insert
          #insert into logged_in
          #return true or false

class Groups(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          phone = json_data['phone']
          print(phone)
          group_name = json_data['group_name']
          print(group_name)
          cursor = db.cursor()
          sql = "INSERT INTO Groups(group_name) VALUES(%s)"
          cursor.execute(sql,group_name)
          cursor.execute("SELECT LAST_INSERT_ID()")
          group_id = cursor.fetchone()[0]
          db.commit()
          member_doc = json_data['members']
          l = []
          l.append((group_id,phone))
          for member in member_doc:
               number = member['phone']
               l.append((group_id,number))
          sql = "INSERT INTO GroupUsers VALUES(%s,%s)"
          cursor.executemany(sql,l)
          print(l)
          db.commit()
          return True
#
class PersonGroups(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          phone = json_data['phone']
          cursor = db.cursor()
          sql = "SELECT Groups.* FROM Groups INNER JOIN GroupUsers WHERE Groups.group_id=GroupUsers.group_id AND GroupUsers.phone=%s"
          cursor.execute(sql,phone)
          all_groups = cursor.fetchall()
          l=[]
          for group in all_groups:
               d={}
               d['group_id']=group[0]
               d['group_name']=group[1]
               l.append(d)
          j = jsonify(l)
          print(j)
          return j
#{"members":[]}
class GroupTransactions(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          group_id = json_data['group_id']
          cursor = db.cursor()
          sql = "SELECT Users.phone,Users.name FROM Users INNER JOIN GroupUsers WHERE Users.phone=GroupUsers.phone AND GroupUsers.group_id=%s"
          cursor.execute(sql,group_id)
          all_members = cursor.fetchall()
          m = []
          for member in all_members:
               d={}
               d['phone'] = member[0]
               d['name'] = member[1]
               m.append(d)
          sql = "SELECT from_id,to_id,amount,date,description FROM Transactions WHERE group_id=%s order by date desc"
          cursor.execute(sql,group_id)
          all_transactions = cursor.fetchall()
          t=[]
          for trans in all_transactions:
               d={}
               d['from_id'] = trans[0]
               d['to_id'] = trans[1]
               d['amount'] = trans[2]
               d['date'] = trans[3]
               d['description'] = trans[4]
               t.append(d)
          res = {}
          res['members'] = m
          res['transactions'] = t
          print(res)
          return jsonify(res)

class PersonalTransactions(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          phone = json_data['phone']
          print(phone)
          cursor = db.cursor()
          sql = "SELECT * FROM v WHERE from_id = %s or to_id = %s ORDER BY date desc "
          cursor.execute(sql,(phone,phone))
          all_transactions = cursor.fetchall()
          t=[]
          for trans in all_transactions:
               d = {}
               d['from_name'] = trans[0]
               d['to_name'] = trans[1]
               d['from_id'] = trans[2]
               d['to_id'] = trans[3]
               d['amount'] = trans[4]
               d['date'] = trans[5]
               d['description'] = trans[6]
               t.append(d)
          print(t)
          return jsonify(t)

class AddTransactions(Resource):
     def post(self):
          json_data = request.get_json(force=True)
          json_array = json_data['transactions']
          phone = json_data['phone']
          group_id = json_data['group_id']
          desc = json_data['desc']
          t = []
          for json_data in json_array:
                from_id = json_data['id']
                amount = json_data['amount']
                list_entry = (from_id,phone,amount,group_id,desc)
                t.append(list_entry)
          cursor = db.cursor()
          sql = "INSERT INTO Transactions(from_id,to_id,amount,date,group_id,description) VALUES(%s,%s,%s,sysdate(),%s,%s) "
          cursor.executemany(sql,t)
          sql  = "UPDATE "
          db.commit()
          return "done"



api.add_resource(Groups,'/group')
api.add_resource(AddTransactions,'/add_trans')
api.add_resource(Login,'/login')
api.add_resource(Register,'/register')
api.add_resource(PersonGroups,'/person_group')
api.add_resource(GroupTransactions,'/group_trans')
api.add_resource(PersonalTransactions,'/personal_trans')
