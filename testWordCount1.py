# -*- coding:utf-8 -*-

import os
import types
import tempfile
import subprocess
import unittest
from HTMLTestRunner_Chart import HTMLTestRunner

root = os.getcwd()

# 输入输出标准答案文件文件夹路径
input_dir = os.path.join(root, "input_work1")
output_dir = os.path.join(root, "output_work1")

# src源文件路径（包含Main.java、Lib.java、result.txt）
src = '"221600219&221600212"\\src\\'
src_path = os.path.join(root, src).replace('\\', '/')
cwd = src_path.replace('"', '')
result_path = cwd + "result.txt"

# 编译、运行指令
compile_command = "javac Main.java"
base_run_command = "java Main "

# 超时/秒
timeout = 2

# 参数驱动的单元测试
class ParametrizedTestCase(unittest.TestCase):
    """ TestCase classes that want to be parametrized should
        inherit from this class.
    """
    def __init__(self, methodName='runTest', input_name=None, output_name=None):
        super(ParametrizedTestCase, self).__init__(methodName)
        self.input_name = input_name
        self.output_name = output_name

    @staticmethod
    def parametrize(testcase_klass, input_name=None, output_name=None):
        """ Create a suite containing all tests taken from the given
            subclass, passing them the parameter 'param'.
        """
        testloader = unittest.TestLoader()
        testnames = testloader.getTestCaseNames(testcase_klass)
        suite = unittest.TestSuite()
        for name in testnames:
            suite.addTest(testcase_klass(name,
                    input_name=input_name, output_name=output_name))
        return suite

# 测试编译
class WordCount1TestCase1(unittest.TestCase):

    def test_compile(self):
        with tempfile.TemporaryFile(mode="w+") as fcompile:
            print("\n测试编译 " + compile_command)
            popen = subprocess.Popen(compile_command, shell=True,
                                stdout=fcompile, stderr=fcompile, cwd=cwd)
            popen.wait(timeout=timeout)
            fcompile.seek(0)
            fcontent = fcompile.read()
            self.assertTrue(len(fcontent) == 0,
                                msg="\n命令行出现输出:\n"+fcontent)

# 测试运行
class WordCount1TestCase2(ParametrizedTestCase):

    def test_input(self):
        input_name = self.input_name
        output_name = self.output_name
        input_path = os.path.join(input_dir, input_name)
        output_path = os.path.join(output_dir, output_name)
        cmd = base_run_command + input_path
        print("\n测试输入文件：%s\n运行指令：%s" % (input_path, cmd))
        with tempfile.TemporaryFile(mode="w+") as frun:
            popen = subprocess.Popen(cmd, shell=True,
                                stdout=frun, stderr=frun, cwd=cwd)
            popen.wait(timeout=timeout)
            frun.seek(0)

            fcontent = frun.read()
            print(fcontent)
            self.assertTrue(len(fcontent) == 0,
                                msg="\n命令行出现输出：\n"+fcontent)

        with open(output_path, "r", encoding="utf-8") as fout:
            with open(result_path, "r", encoding="utf-8") as fres:
                # 换行符统一，去除尾部空行
                fout_content = fout.read().replace("\n", "\r\n").rstrip()
                fres_content = fres.read().replace("\n", "\r\n").rstrip()
                print("\n标准输出：\n%s" % fout_content)
                print("\n程序输出：\n%s" % fres_content)
                self.assertEqual(fout_content, fres_content,
                                msg="\n输出结果不一致")

# 载入input、result标准文件
def loadFile(files_dir):
    temp = []
    for root_, dirs, files in os.walk(files_dir):
        for file in files:
            # 遍历所有文件
            file = os.path.join(root,
                        os.path.join(root_, file)).replace('\\', '/')
            if os.path.splitext(file)[1] == ".txt":
                temp.append(file)
    return temp

def Suite():
    suite = unittest.TestSuite()
    suite.addTest(WordCount1TestCase1("test_compile"))
    input_files = loadFile(input_dir)
    output_files = loadFile(output_dir)
    # 自动载入所有测试文件
    for inputf, outputf in zip(input_files, output_files):
        suite.addTest(ParametrizedTestCase.parametrize(
            WordCount1TestCase2,
            input_name=inputf,
            output_name=outputf
            )
        )
    return suite

if __name__ == '__main__':
    runner = HTMLTestRunner(
        title="WordCount热词统计基础版的测试报告",
        description="福州大学软工实践第二次结对作业：热词统计基础版自动测试",
        stream=open("./test_wordcount_result1.html", "wb"),
        verbosity=2,
        retry=0,
        save_last_try=True)
    runner.run(Suite())