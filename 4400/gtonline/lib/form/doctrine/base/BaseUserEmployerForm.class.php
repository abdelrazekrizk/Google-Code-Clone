<?php

/**
 * UserEmployer form base class.
 *
 * @method UserEmployer getObject() Returns the current form's model object
 *
 * @package    gtonline
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormGeneratedTemplate.php 29553 2010-05-20 14:33:00Z Kris.Wallsmith $
 */
abstract class BaseUserEmployerForm extends BaseFormDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'         => new sfWidgetFormInputHidden(),
      'userId'     => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('User'), 'add_empty' => false)),
      'employerId' => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('Employer'), 'add_empty' => false)),
      'jobTitle'   => new sfWidgetFormInputText(),
    ));

    $this->setValidators(array(
      'id'         => new sfValidatorChoice(array('choices' => array($this->getObject()->get('id')), 'empty_value' => $this->getObject()->get('id'), 'required' => false)),
      'userId'     => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('User'))),
      'employerId' => new sfValidatorDoctrineChoice(array('model' => $this->getRelatedModelName('Employer'))),
      'jobTitle'   => new sfValidatorString(array('max_length' => 255)),
    ));

    $this->widgetSchema->setNameFormat('user_employer[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'UserEmployer';
  }

}
