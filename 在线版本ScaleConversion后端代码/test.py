# -*- coding: utf-8 -*-
# @Time    : 2023/4/9 21:43
# @Author  : qxcnwu
# @FileName: test.py
# @Software: PyCharm
import json
import sys

from WinForm.CommonObject.InputObject import InputObject
from WinForm.CommonObject.MessageLogger import MsgConnecter, MessageLogger
from WinForm.DataProcess.ProcessMain import Process
from WinForm.DataProcess.SedProcess import decode_sed


class MsgConnecteres(MsgConnecter):
    def __int__(self, lev=1):
        super().__init__(lev)
        pass

    def add_msg(self, msg: str):
        print(msg)

    def msg_type(self, msg: str) -> str:
        return msg


msg = MessageLogger(MsgConnecteres(1), 1)


def save_ans(inobj: InputObject, save_path: str):
    def read():
        wave = None
        ref = []
        for file in inobj.ref:
            wave, re = decode_sed(file)
            ref.append(re.tolist())
        return wave, ref

    save_path = save_path.replace("D:", "C:")
    ans = {}
    ans["vis"] = inobj.vis_ans.tolist()
    ans["nir"] = inobj.nir_ans.tolist()
    ans["ans"] = inobj.ans.tolist()
    wave, re = read()
    ans["wave"] = wave.tolist()
    ans["seds"] = re
    print(ans)
    print(save_path)
    with open(save_path, "w") as f:
        f.write(json.dumps(ans))
    f.close()

    return


def main(json_p: str):
    """

    :param json_p:
    :return:
    """
    with open(json_p, "r") as wd:
        obj = json.loads(wd.read())
    wd.close()

    inObj = InputObject()
    inObj.save_path = "./"
    inObj.vis_path = obj["visPic"]
    inObj.nir_path = obj["nirPic"]

    print(inObj)

    for i in range(len(obj["seds"].keys())):
        inObj.ref.append(obj["seds"][str(i)])

    inObj.vis_square = []
    for li in obj["visLoc"]:
        inObj.vis_square.append([li[1], li[0], li[3], li[2]])

    inObj.nir_square = []
    for li in obj["nirLoc"]:
        inObj.nir_square.append([li[1], li[0], li[3], li[2]])
    inObj = Process(inObj)
    save_ans(inObj, obj["savePath"])

    return


if __name__ == '__main__':
    main(sys.argv[1])
