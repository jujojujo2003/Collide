#    Copyright 2012 S.Lakshminarayanan (www.s-ln.in)
#    This file is part of Collide.
#
#    Collide is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    Collide is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with Collide.  If not, see <http://www.gnu.org/licenses/>.
from Tkinter import *
import tkFileDialog

class App:
    ClickColor="blue"
    def __init__(self, master):

        frame = Frame(master)
        frame.pack()
        self.button=[]
        for i in range(20):
            for j in range(30):
             temp = Button(frame, text="-",bg="grey", width="4",height="2",command=lambda no=30*i+j:self.process_button(no))
             temp.grid(row=i,column=j)
             temp.followups=[]
             self.button=self.button+[temp]
        
        self.hi_there = Button(frame, text="Hello")
        Label(frame,text="grey(-) for nothing").grid(row=2,column=35)
        temp = Button(frame, text="Save",command=self.save)
        temp.grid(row=5,column=35)
        temp = Button(frame, text="Load",command=self.load)
        temp.grid(row=7,column=35)
        temp = Button(frame, text="New",command=self.reset)
        temp.grid(row=9,column=35)
    def reset(self):
        for no in range(30*20):
           self.button[no]['fg']="black"
           self.button[no]['bg']="grey"
           self.button[no]['text']="-"
    def save(self):
        fileptr=tkFileDialog.asksaveasfile(mode='w')
        for i in self.button:
            fileptr.write(i['text']+" ")
        fileptr.close()
    def load(self):
        fileptr=tkFileDialog.askopenfile(mode="r")
        filedata=fileptr.read()
        filedata=filedata.split()
        no=0
        for i in filedata:
            self.button[no]['text']=i
            if i == "-":
                self.button[no]['fg']="black"
                self.button[no]['bg']="grey"
            elif i == "user":
                self.button[no]['fg']="black"
                self.button[no]['bg']="cyan"
            elif i == "wall":
                self.button[no]['fg']="black"
                self.button[no]['bg']="yellow"
            elif i == "hole":
                self.button[no]['fg']="white"
                self.button[no]['bg']="black"
            elif i == "enemy" or "stat":
                self.button[no]['fg']="black"
                self.button[no]['bg']="red"
            elif i == "goal":
                self.button[no]['fg']="black"
                self.button[no]['bg']="green"
            no = no + 1
    def process_button(self,no):
        col=no%21
        row=no/20
        text=self.button[no]['text']
        if text=="-":
            self.button[no]['fg']="black"
            self.button[no]['text']="goal"
            self.button[no]['bg']="green"
        elif text=="goal":
            self.button[no]['fg']="black"
            self.button[no]['text']="enemy"
            self.button[no]['bg']="red"
        elif text=="enemy":
            self.button[no]['fg']="white"
            self.button[no]['text']="hole"
            self.button[no]['bg']="black"
        elif text=="hole":
            self.button[no]['fg']="black"
            self.button[no]['text']="wall"
            self.button[no]['bg']="yellow"
        elif text=="wall":
            self.button[no]['fg']="black"
            self.button[no]['text']="user"
            self.button[no]['bg']="cyan"
        elif text=="user":
            self.button[no]['fg']="black"
            self.button[no]['text']="stat"
            self.button[no]['bg']="red"
        elif text=="stat":
            self.button[no]['fg']="black"
            self.button[no]['text']="-"
            self.button[no]['bg']="grey"    

root = Tk()

app = App(root)

root.mainloop()
