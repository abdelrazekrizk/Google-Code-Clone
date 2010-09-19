<?php

/**
 * UserSchool form base class.
 *
 * @method UserSchool getObject() Returns the current form's model object
 *
 * @package    gtonline
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormGeneratedTemplate.php 29553 2010-05-20 14:33:00Z Kris.Wallsmith $
 */
abstract class BaseUserSchoolForm extends BaseFormDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'             => new sfWidgetFormInputHidden(),
      'userId'         => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('User'), 'add_empty' => false)),
      'schoolId'       => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('School'), 'add_empty' => false)),
      'graduationYear' => new sfWidgetFormInputText(),
    ));

    $this->setValidators(array(
      'id'             => new sfValidatorChoice(array('choices' => array($this->getObject()->get('id')), 'empty_value' => $this->getObject()->get('id'), 'required' => false)),
      'userId'         => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('User'))),
      'schoolId'       => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('School'))),
      'graduationYear' => new sfValidatorInteger(array('required' => false)),
    ));

    $this->widgetSchema->setNameFormat('user_school[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'UserSchool';
  }

}
