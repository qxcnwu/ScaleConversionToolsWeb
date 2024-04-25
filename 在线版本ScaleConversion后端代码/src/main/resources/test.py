# -*- coding: utf-8 -*-
# @Time    : 2023/4/9 21:43
# @Author  : qxcnwu
# @FileName: test.py
# @Software: PyCharm
import json
import sys

from ScaleConvertionTools.CommonObject.InputObject import InputObject
from ScaleConvertionTools.CommonObject.MessageLogger import MsgConnecter, MessageLogger
from ScaleConvertionTools.DataProcess.ProcessMain import Process
from ScaleConvertionTools.DataProcess.SedProcess import decode_sed


class MsgConnecteres(MsgConnecter):
    def __int__(self, lev=1):
        super().__init__(lev)
        pass


msg = MessageLogger(MsgConnecteres(1), 1)


def save_ans(inobj: InputObject, save_path: str):
    def read():
        wave = None
        ref = []
        for file in inobj.ref:
            wave, re = decode_sed(file)
            ref.append(re.tolist())
        return wave, ref

    ans = {}
    ans["vis"] = inobj.vis_ans
    ans["nir"] = inobj.nir_ans
    ans["ans"] = inobj.ans
    wave, re = read()
    ans["wave"] = wave.tolist()
    ans["seds"] = re

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
    for i in range(len(obj["seds"].keys())):
        inObj.ref.append(obj["seds"][str(i)])
    inObj.vis_square = obj["visLoc"]
    inObj.nir_square = obj["nirLoc"]
    Process(inObj)
    save_ans(inObj, obj["savePath"])

    return


if __name__ == '__main__':
    main(sys.argv[1])
